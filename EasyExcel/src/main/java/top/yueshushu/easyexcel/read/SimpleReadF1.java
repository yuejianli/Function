package top.yueshushu.easyexcel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.easyexcel.handler.NoObjectReadHandler;
import top.yueshushu.easyexcel.handler.SimpleReadHandler;
import top.yueshushu.easyexcel.readpojo.Person;
import top.yueshushu.easyexcel.service.PersonService;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName:SimpleRead
 * @Description 简单的读取Excel 文件， 读的时候，是按照顺序来的。
 * @Author yjl
 * @Date 2021/6/7 17:26
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class SimpleReadF1 {
    @Resource
    private PersonService personService;

    /**
     * 主要用于，分批进行处理读取的数据信息。
     * 异步读取
     *
     * @throws Exception
     */
    @Test
    public void simpleReadTest() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcel.xlsx");
        InputStream inputStream = classPathResource.getInputStream();

//        File file1=new File("src\\main\\resources\\easyexcel.xlsx");
//        InputStream inputStream=new FileInputStream(file1);
        //EasyExcel 调用 read 方法，进行读取， 获取sheet 第一个表格页。
        EasyExcel.read(inputStream,
                        Person.class, new SimpleReadHandler(personService))
                .sheet().doRead();
    }

    @Test
    public void simpleReadTest2() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcel.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        //采用 ExcelRead 对象方法，进行处理.
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream, Person.class,
                    new SimpleReadHandler(personService)).build();
            //进行处理，获取到第一个表格. 可以按照编号读，可以按照名称进行读取。
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } catch (Exception e) {

        } finally {
            if (excelReader != null) {
                //这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }

    /**
     * 不创建对象的读取。
     *
     * @throws Exception
     */
    @Test
    public void simpleReadTest3() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcel.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        EasyExcel.read(inputStream, new NoObjectReadHandler())
                .sheet().doRead();
    }


    @Test
    public void simpleReadTest4() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcel.xlsx");
        InputStream inputStream = classPathResource.getInputStream();

        // 读取数据，进行处理。
        EasyExcel.read(inputStream,
                        Person.class, new PageReadListener<Person>(
                                dataList -> {
                                    for (Person person : dataList) {
                                        // 具体的处理信息
                                        log.info(">>> 读取数据: {}", JSON.toJSONString(person));
                                    }
                                }
                        ))
                .sheet().doRead();
    }

    /**
     * 匿名内部类进行读取信息.
     * 其实就是  SimpleReadHandler
     */
    @Test
    public void simpleReadTest5() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcel.xlsx");
        InputStream inputStream = classPathResource.getInputStream();

        // 读取数据，进行处理。
        EasyExcel.read(inputStream,
                        Person.class, new ReadListener<Person>() {
                            private final Integer BATCH_READ = 5;
                            //批量保存的数据集合。
                            private List<Person> batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);

                            /**
                             * 对解析的每一条数据，进行处理。 data 时，已经处理完成了。
                             * @param data
                             * @param context
                             */
                            @Override
                            public void invoke(Person data, AnalysisContext context) {
                                log.info("解析读取数据>>>>" + data);
                                batchSaveDataList.add(data);
                                if (batchSaveDataList.size() >= BATCH_READ) {
                                    personService.saveData(batchSaveDataList);
                                    // 存储完成清理 list
                                    batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);
                                }
                            }

                            /**
                             * 解析完成后的数据。
                             * @param context
                             */
                            @Override
                            public void doAfterAllAnalysed(AnalysisContext context) {
                                log.info(">>>>>>>>>解析数据完成");
                                //再次保存一下，避免后面不大于5时，依旧能保存信息。
                                personService.saveData(batchSaveDataList);
                                batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);
                            }
                        })
                .sheet().doRead();
    }

}
