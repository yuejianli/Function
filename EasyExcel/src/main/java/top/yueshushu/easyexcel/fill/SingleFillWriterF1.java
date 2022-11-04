package top.yueshushu.easyexcel.fill;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.fillpojo.FillPerson;
import top.yueshushu.easyexcel.util.WriteDataUtil;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:SingleFillWriterF1
 * @Description 单个数据填充
 * @Author yjl
 * @Date 2021/6/9 9:48
 * @Version 1.0
 **/
@SpringBootTest
public class SingleFillWriterF1 {
    /**
     * 对象 obj 进行填充。
     */
    @Test
    public void objFillTest() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "fill_template.xlsx");
        InputStream tempInputStream = new FileInputStream(file);
        //进行填充.
        File objFile = new File(WriteDataUtil.FILE_PATH + "fill_obj.xlsx");
        OutputStream objOutputStream = new FileOutputStream(objFile);
        //放置值。
        FillPerson fillPerson = new FillPerson();
        fillPerson.setId(1);
        fillPerson.setName("两个蝴蝶飞");
        fillPerson.setAge(24);
        fillPerson.setBirthday(new Date());

        EasyExcel.write(objOutputStream)
                .withTemplate(tempInputStream)
                //不能指定 sheet的名称
                .sheet(0)
                .doFill(fillPerson);
    }

    /**
     * 对象 map 进行填充。
     */
    @Test
    public void mapFillTest() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "fill_template.xlsx");
        InputStream tempInputStream = new FileInputStream(file);
        //进行填充.
        File objFile = new File(WriteDataUtil.FILE_PATH + "fill_map.xlsx");
        OutputStream objOutputStream = new FileOutputStream(objFile);
        //放置值。
        Map<String, Object> fillMap = new HashMap<>();
        fillMap.put("id", 2);
        fillMap.put("name", "两个蝴蝶飞");
        fillMap.put("age", 26);
        fillMap.put("birthday", new Date());

        EasyExcel.write(objOutputStream)
                .withTemplate(tempInputStream)
                //不能指定 sheet的名称
                .sheet(0)
                .doFill(fillMap);
    }

}
