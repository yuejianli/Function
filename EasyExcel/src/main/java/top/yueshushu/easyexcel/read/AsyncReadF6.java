package top.yueshushu.easyexcel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.easyexcel.readpojo.Person;
import top.yueshushu.easyexcel.service.PersonService;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
public class AsyncReadF6 {

    @Resource
    private PersonService personService;

    @Test
    public void readTest() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcel.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        System.out.println(">>>>>>>>>>>>>>读取到List里面");
        //同步进行读取数据，会放置在内存里面.
        List<Person> personList = EasyExcel.read(inputStream).head(Person.class).
                sheet(0).doReadSync();
        for (Person person : personList) {
            log.info(JSON.toJSONString(person));
        }
        //也可以读取出 List Map里面。
        log.info(">>>>>>>>>>>读取到Map里面");
        classPathResource = new ClassPathResource("easyexcelsince.xlsx");
        inputStream = classPathResource.getInputStream();
        List<Map<Integer, String>> listMap = EasyExcel.read(inputStream).sheet(0).doReadSync();
        for (Map<Integer, String> rowMap : listMap) {
            log.info("读取信息:{}", rowMap);
        }
    }
}
