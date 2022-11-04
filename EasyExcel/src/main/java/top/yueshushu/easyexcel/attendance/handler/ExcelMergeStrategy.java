package top.yueshushu.easyexcel.attendance.handler;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @ClassName:ExcelMergeStrategy
 * @Description 合并策略
 * @Author zk_yjl
 * @Date 2021/9/17 15:19
 * @Version 1.0
 * @Since 1.0
 **/
public class ExcelMergeStrategy implements CellWriteHandler {
    /**
     * 合并起始行
     */
    private int mergeRowIndex;

    /**
     * 多少行合并一次
     */
    private int eachRow;
    /**
     * 合并字段的下标
     */
    private int[] mergeColumnIndex;

    public ExcelMergeStrategy(int mergeRowIndex, int[] mergeColumnIndex, int eachRow) {
        if (mergeRowIndex < 0) {
            throw new IllegalArgumentException("mergeRowIndex must be greater than 0");
        }
        if (eachRow < 0) {
            throw new IllegalArgumentException("eachRow must be greater than 0");
        }
        this.mergeRowIndex = mergeRowIndex;
        this.mergeColumnIndex = mergeColumnIndex;
        this.eachRow = eachRow;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, WriteCellData<?> cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        int curRowIndex = cell.getRowIndex();
        //当前列
        int curColIndex = cell.getColumnIndex();
        //合并条件：
        //1.当前行>合并起始行，默认标题行（0）不参加合并
        //2.间隔行（eachRow）的上下两条不参加合并
        //2.1间隔行（eachRow）==0时，不设置间隔
        if (isMerge(curRowIndex)) {
            IntStream.range(0, mergeColumnIndex.length).anyMatch(i -> {
                //存在这一列，进行合并。
                if (curColIndex == mergeColumnIndex[i]) {
                    mergeWithPrevRow(writeSheetHolder, cellData, cell, curRowIndex, curColIndex);
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {

    }

    /**
     * 判断是否合并
     * 1.当前位置必须大于开始位置：curRowIndex > mergeRowIndex
     * 2.根据eachRow 判断数据分割的间隔
     * 2.1如果根据eachRow=0，默认不合并
     * 2.2如果1如果根据eachRow>0,分割后的第一条数据不会与之前的合并：（curRowIndex-mergeRowIndex）%eachRow==0
     *
     * @return
     */
    private boolean isMerge(Integer curRowIndex) {

        if ((curRowIndex > mergeRowIndex) && eachRow > 0) {
            if ((curRowIndex - mergeRowIndex) % eachRow == 0) {
                return false;
            }
            return true;
        }

        return false;
    }

    private void mergeWithPrevRow(WriteSheetHolder writeSheetHolder, CellData cellData, Cell cell, int curRowIndex, int curColIndex) {

        // 比较当前行的第一列的单元格与上一行是否相同，相同合并当前单元格与上一行
        if (compareValueIsSame(cellData, cell, curRowIndex, curColIndex)) {
            Sheet sheet = writeSheetHolder.getSheet();
            List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
            boolean isMerged = false;
            for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
                CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                // 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
                if (cellRangeAddr.isInRange(curRowIndex - 1, curColIndex)) {
                    sheet.removeMergedRegion(i);
                    cellRangeAddr.setLastRow(curRowIndex);
                    sheet.addMergedRegion(cellRangeAddr);
                    isMerged = true;
                }
            }
            // 若上一个单元格未被合并，则新增合并单元
            if (!isMerged) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex - 1, curRowIndex, curColIndex,
                        curColIndex);
                sheet.addMergedRegion(cellRangeAddress);
            }
        }
    }

    /**
     * 比较两个前后两个值是否相同
     *
     * @param cellData
     * @param cell
     * @param curRowIndex
     * @param curColIndex
     * @return boolean
     * @date 2021/9/17 16:54
     * @author zk_yjl
     */
    private boolean compareValueIsSame(CellData cellData, Cell cell, int curRowIndex, int curColIndex) {

        //获取当前行的当前列的数据和上一行的当前列列数据，通过上一行数据是否相同进行合并
        Cell preCell = cell.getSheet().getRow(curRowIndex - 1).getCell(curColIndex);
        //对数据进行转换。转换成 String 类型。
        String curData = "";
        if (CellDataTypeEnum.STRING == cellData.getType()) {
            curData = cellData.getStringValue();
        } else {
            BigDecimal tempBigDecimal = cellData.getNumberValue();
            curData = tempBigDecimal.stripTrailingZeros().toPlainString();
        }
        if (StringUtils.isEmpty(curData)) {
            return false;
        }
        String preData = "";
        if (CellType.STRING == preCell.getCellTypeEnum()) {
            preData = preCell.getStringCellValue();
        } else {
            double dv = preCell.getNumericCellValue();
            BigDecimal tempBigDecimal = new BigDecimal(String.valueOf(dv));
            preData = tempBigDecimal.stripTrailingZeros().toPlainString();
        }
        if (StringUtils.isEmpty(preData)) {
            return false;
        }
        //不为空且相等时，才进行合并。
        if (curData.equals(preData)) {
            return true;
        }
        return false;

    }

}
