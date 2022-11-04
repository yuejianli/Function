package top.yueshushu.easyexcel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import top.yueshushu.easyexcel.handler.ConvertReadHandler;
import top.yueshushu.easyexcel.readpojo.ConvertData;

import java.io.InputStream;

/**
 * @ClassName:ConvertRead
 * @Description 日期、数字或者自定义格式转换
 * @Author yjl
 * @Date 2021/6/7 19:23
 * @Version 1.0
 **/
@SpringBootTest
public class ConvertReadF4 {
    @Test
    public void convertRead() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcelconvert.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream, ConvertData.class, new ConvertReadHandler())
                    .build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                //需要关闭
                excelReader.finish();
            }
        }
    }

    @Test
    public void convertRead2Test() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcelconvert.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream, ConvertData.class, new ConvertReadHandler())
                    //多行头，默认从1开始。
                    .headRowNumber(3)
                    .build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                //需要关闭
                excelReader.finish();
            }
        }
    }
}
