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
import top.yueshushu.easyexcel.handler.SimpleReadWithHeadHandler;
import top.yueshushu.easyexcel.readpojo.Person;
import top.yueshushu.easyexcel.service.PersonService;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * @ClassName:listRead
 * @Description 同步读取数据信息
 * @Author yjl
 * @Date 2021/6/7 19:37
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ReadHeaderF7 {

    @Resource
    private PersonService personService;

    //读取时，解析表头的数据。
    @Test
    public void simpleReadTest2() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcel.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        //采用 ExcelRead 对象方法，进行处理.
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream, Person.class,
                    new SimpleReadWithHeadHandler(personService)).build();
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
