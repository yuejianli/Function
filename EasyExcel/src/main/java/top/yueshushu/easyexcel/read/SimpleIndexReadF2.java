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
 * @ClassName:SimpleRead
 * @Description 读索引信息匹配信息，按照 Index 或者 Name 进行读取。
 * @Author yjl
 * @Date 2021/6/7 17:26
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class SimpleIndexReadF2 {

    @Resource
    private PersonService personService;

    /**
     * 主要用于，分批进行处理读取的数据信息。
     *
     * @throws Exception
     */
    @Test
    public void simpleReadTest() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcel.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        //EasyExcel 调用 read 方法，进行读取， 获取sheet 第一个表格页。
        EasyExcel.read(inputStream,
                        PersonIndex.class, new IndexOrNameReadHandler(personService))
                .sheet().doRead();
    }

    @Test
    public void simpleReadTest2() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcel.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        //采用 ExcelRead 对象方法，进行处理.
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream, PersonIndex.class,
                    new IndexOrNameReadHandler(personService)).build();
            //进行处理，获取到第一个表格. 可以按照编号读，可以按照名称进行读取。
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } catch (Exception e) {

        } finally {
            if (excelReader != null) {
                //千万不要忘记关闭，用 finish 进行完成。
                excelReader.finish();
            }
        }
    }
}
