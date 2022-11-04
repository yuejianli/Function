package top.yueshushu.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.ConvertWrite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @ClassName:WriteSimple
 * @Description 日期、数字或者自定义格式转换
 * @Author yjl
 * @Date 2021/6/8 9:24
 * @Version 1.0
 **/
@SpringBootTest
public class ConvertWriteSimpleF5 {
    @Test
    public void convertWrite() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_convert.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, ConvertWrite.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "转换")
                    .build();
            //填入数据
            excelWriter.write(WriteDataUtil.getConvertData(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }
}
