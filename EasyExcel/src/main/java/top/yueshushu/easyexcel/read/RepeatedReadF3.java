package top.yueshushu.easyexcel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.easyexcel.handler.IndexOrNameReadHandler;
import top.yueshushu.easyexcel.readpojo.PersonIndex;
import top.yueshushu.easyexcel.service.PersonService;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * @ClassName:repeatedRead
 * @Description 读多个或者全部sheet, 这里注意一个sheet不能读取多次，多次读取需要重新读取文件
 * @Author yjl
 * @Date 2021/6/7 19:04
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class RepeatedReadF3 {

    @Resource
    private PersonService personService;

    @Test
    public void repeatedReadTest() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcelmore.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        EasyExcel.read(inputStream, PersonIndex.class, new IndexOrNameReadHandler(personService))
                .doReadAll();
    }

    @Test
    public void repeatedRead2Test() throws Exception {
        ClassPathResource classpathResource = new ClassPathResource("easyexcelmore.xlsx");
        InputStream inputStream = classpathResource.getInputStream();
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream).build();
            //注册Sheet,可以进行分别的注册和读取，用于不同的sheet页里面不同的内容的情况。
            ReadSheet readSheet1 = EasyExcel.readSheet(0)
                    .head(PersonIndex.class).registerReadListener(new IndexOrNameReadHandler(personService)).build();
            // 读取第2个。
            ReadSheet readSheet2 = EasyExcel.readSheet(1)
                    .head(PersonIndex.class).registerReadListener(new IndexOrNameReadHandler(personService)).build();
            //需要一起传入进来。
            excelReader.read(readSheet1, readSheet2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != excelReader) {
                excelReader.finish();
            }
        }

    }

}
