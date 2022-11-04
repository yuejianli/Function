package top.yueshushu.easyexcel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import lombok.extern.log4j.Log4j2;
import top.yueshushu.easyexcel.readpojo.ExtraSince;

import java.util.Map;

/**
 * @ClassName:ExtraSinceReadHandler
 * @Description 读取批注，超链接和合并单元格时使用
 * @Author yjl
 * @Date 2021/6/7 19:59
 * @Version 1.0
 **/
@Log4j2
public class ExtraSinceReadHandler extends AnalysisEventListener<ExtraSince> {
    @Override
    public void invoke(ExtraSince data, AnalysisContext context) {
        log.info("读取到的数据是{}", data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("读取数据完成");
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("读取到表头数据{}", headMap);
        super.invokeHeadMap(headMap, context);
    }

    /**
     * 解析批注和超链接，合并单元格信息
     *
     * @param extra
     * @param context
     */
    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        log.info("读取到额外的信息{}", extra);
        switch (extra.getType()) {
            //是批注的信息
            case COMMENT: {
                log.info("批注信息，在rowIndex-->{},columnIndex-->{},内容是-->:{}",
                        extra.getRowIndex(), extra.getColumnIndex(),
                        extra.getText());
                break;
            }
            //是超链接信息
            case HYPERLINK: {
                log.info("是超链接信息，在rowIndex-->{},columnIndex-->{},内容是-->:{}",
                        extra.getRowIndex(), extra.getColumnIndex(),
                        extra.getText());
                break;
            }
            //是合并信息
            case MERGE: {
                log.info("合并的信息，覆盖区间，在 firstRowIndex-->{},firstColumnIndex-->{}," +
                                "lastRowIndex-->{},lastColumnIndex-->{},内容是---->{}",
                        extra.getFirstRowIndex(), extra.getFirstColumnIndex(),
                        extra.getLastRowIndex(), extra.getLastColumnIndex(),
                        extra.getText());
                break;
            }
        }
    }
}
