package top.yueshushu.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.WritePerson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @ClassName:AutoWidthF12
 * @Description 自动列宽
 * @Author yjl
 * @Date 2021/6/8 10:59
 * @Version 1.0
 **/
@SpringBootTest
public class AutoWidthF12 {
    @Test
    public void dynamicWithWrite() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_width.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, WritePerson.class)
                    //宽度自动适应
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "自动宽度")
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
