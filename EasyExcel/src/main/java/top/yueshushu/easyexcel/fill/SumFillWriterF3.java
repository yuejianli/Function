package top.yueshushu.easyexcel.fill;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.WritePerson;

import java.io.*;
import java.util.*;

/**
 * @ClassName:SumFillWriter
 * @Description 复杂填充
 * @Author yjl
 * @Date 2021/6/9 17:36
 * @Version 1.0
 **/
@SpringBootTest
public class SumFillWriterF3 {
    /**
     * 模板最后一行有值，必须一次性内存中处理。
     *
     * @throws Exception
     */
    @Test
    public void listFillTest() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "fill_template_sum.xlsx");
        InputStream tempInputStream = new FileInputStream(file);
        //进行填充.
        File objFile = new File(WriteDataUtil.FILE_PATH + "fill_sum.xlsx");
        OutputStream objOutputStream = new FileOutputStream(objFile);
        List<WritePerson> personList = WriteDataUtil.getData();
        //进行配置
        ExcelWriter excelWriter = EasyExcel.write(objOutputStream)
                .withTemplate(tempInputStream)
                .build();
        //创建Sheet 页
        WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
        //设置配置，如果list最后有数据，需要用 forceNewRow 为 true
        FillConfig fillConfig = FillConfig.builder()
                .forceNewRow(true).build();
        //进行填充数据
        excelWriter.fill(personList, fillConfig, writeSheet);
        excelWriter.fill(personList, fillConfig, writeSheet);
        //用Map 填充其他的值,根据参数。
        Map<String, Object> map = new HashMap<>();
        map.put("date", new Date());
        map.put("total", 300);
        excelWriter.fill(map, writeSheet);
        excelWriter.finish();
    }

    @Test
    public void listFillTest2() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "fill_template_sum2.xlsx");
        InputStream tempInputStream = new FileInputStream(file);
        //进行填充.
        File objFile = new File(WriteDataUtil.FILE_PATH + "fill_sum2.xlsx");
        OutputStream objOutputStream = new FileOutputStream(objFile);
        List<WritePerson> personList = WriteDataUtil.getData();
        //进行配置
        ExcelWriter excelWriter = EasyExcel.write(objOutputStream)
                .withTemplate(tempInputStream)
                .build();
        //创建Sheet 页
        WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
        //设置配置，如果list最后有数据，需要用 forceNewRow 为 true
        FillConfig fillConfig = FillConfig.builder()
                .build();
        //进行填充数据
        excelWriter.fill(personList, fillConfig, writeSheet);
        excelWriter.fill(personList, fillConfig, writeSheet);
        //用Map 填充其他的值,根据参数。
        Map<String, Object> map = new HashMap<>();
        map.put("date", new Date());
        excelWriter.fill(map, writeSheet);
        //还有一个统计，没有写入的。
        // 这里偷懒直接用list 也可以用对象
        List<List<String>> totalListList = new ArrayList<List<String>>();
        List<String> totalList = new ArrayList<String>();
        totalListList.add(totalList);
        totalList.add(null);
        totalList.add(null);
        totalList.add(null);
        // 第四列
        totalList.add("统计:1000");
        // 这里是write 别和fill 搞错了
        excelWriter.write(totalListList, writeSheet);
        excelWriter.finish();
    }
}
