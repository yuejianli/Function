package top.yueshushu.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.WritePerson;
import top.yueshushu.easyexcel.writepojo.WritePersonFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @ClassName:FormatWrite
 * @Description 注解形式，自定义样式
 * @Author yjl
 * @Date 2021/6/8 10:59
 * @Version 1.0
 **/
@SpringBootTest
public class FormatWriteF9 {

    /**
     * 注解形式定义样式
     */

    @Test
    public void formatWrite() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_format.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, WritePersonFormat.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "格式化列宽和行高")
                    .build();
            //填入数据
            excelWriter.write(WriteDataUtil.getFormatData(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 自定义样式
     */

    @Test
    public void formatSimpleWrite() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_format2.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            /**
             * 处理相关的样式
             */
            WriteCellStyle headerCellStyle = new WriteCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            WriteFont writeFont = new WriteFont();
            writeFont.setFontHeightInPoints((short) 20);
            headerCellStyle.setWriteFont(writeFont);
            //设置内容的相关的信息.
            WriteCellStyle contentCellStyle = new WriteCellStyle();
            contentCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            WriteFont writeFont1 = new WriteFont();
            writeFont1.setFontHeightInPoints((short) 24);
            contentCellStyle.setWriteFont(writeFont1);
            //创建策略
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(
                    headerCellStyle, contentCellStyle
            );
            excelWriter = EasyExcel.write(outputStream, WritePerson.class)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "自定义格式化列宽和行高")
                    .build();
            //填入数据
            excelWriter.write(WriteDataUtil.getData(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }
}
