package top.yueshushu.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.WritePerson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 简单的写入
 *
 * @author yuejianli
 * @date 2022-11-04
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class SimpleWriteF1 {

    /**
     * 填写数据
     */
    @Test
    public void writeTest1() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_simple.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        try {
            EasyExcel.write(outputStream, WritePerson.class)
                    .sheet("简单").doWrite(() -> WriteDataUtil.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void writeTest2() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_simple.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        try {
            EasyExcel.write(outputStream, WritePerson.class)
                    .sheet("简单").doWrite(WriteDataUtil.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入数据
     */
    @Test
    public void writeTest3() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_simple.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, WritePerson.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "简单")
                    .build();
            //填入数据
            excelWriter.write(WriteDataUtil.getData(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

}
