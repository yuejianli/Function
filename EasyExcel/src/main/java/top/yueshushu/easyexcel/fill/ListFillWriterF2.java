package top.yueshushu.easyexcel.fill;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.WritePerson;

import java.io.*;
import java.util.List;

/**
 * @ClassName:FillListWriter
 * @Description List 集合填充数据
 * @Author yjl
 * @Date 2021/6/9 10:09
 * @Version 1.0
 **/
@SpringBootTest
public class ListFillWriterF2 {
    @Test
    public void listFillTest() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "fill_template_list.xlsx");
        InputStream tempInputStream = new FileInputStream(file);
        //进行填充.
        /**
         * 一下子，放置在内存里面，进行填充。
         */
        File objFile = new File(WriteDataUtil.FILE_PATH + "fill_list.xlsx");
        OutputStream objOutputStream = new FileOutputStream(objFile);
        List<WritePerson> personList = WriteDataUtil.getData();
        EasyExcel.write(objOutputStream)
                .withTemplate(tempInputStream)
                //不能指定 sheet的名称
                .sheet(0)
                .doFill(personList);
    }

    /**
     * 分多次 填充 会使用文件缓存（省内存） jdk8
     */
    @Test
    public void listFillTestMore() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "fill_template_list.xlsx");
        InputStream tempInputStream = new FileInputStream(file);
        File objFile = new File(WriteDataUtil.FILE_PATH + "fill_list.xlsx");
        OutputStream objOutputStream = new FileOutputStream(objFile);
        EasyExcel.write(objOutputStream)
                .withTemplate(tempInputStream)
                //不能指定 sheet的名称
                .sheet(0)
                .doFill(() -> WriteDataUtil.getData());
    }

    // 分多次 填充 会使用文件缓存（省内存）
    @Test
    public void listMoreFillTest() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "fill_template_list.xlsx");
        InputStream tempInputStream = new FileInputStream(file);
        //进行填充.
        File objFile = new File(WriteDataUtil.FILE_PATH + "fill_list2.xlsx");
        OutputStream objOutputStream = new FileOutputStream(objFile);
        /**
         * 进行多次填充
         */
        ExcelWriter excelWriter = EasyExcel.write(objOutputStream)
                .withTemplate(tempInputStream)
                .build();
        //创建多个Sheet
        WriteSheet writeSheet = EasyExcel.writerSheet(0)
                .build();
        // 对数据进行分批次填充
        List<WritePerson> personList = WriteDataUtil.getData();

        excelWriter.fill(personList.subList(0, personList.size() / 2), writeSheet);
        excelWriter.fill(personList.subList(personList.size() / 2, personList.size()), writeSheet);
        //需要关闭
        excelWriter.finish();
    }
}
