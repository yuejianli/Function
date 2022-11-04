package top.yueshushu.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.WritePerson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @ClassName:RepeatWrite
 * @Description 多次重复写入
 * @Author yjl
 * @Date 2021/6/8 10:29
 * @Version 1.0
 **/
@SpringBootTest
public class RepeatWriteF4 {
    /**
     * 写入到同一个 Sheet 表格里面。
     *
     * @throws Exception
     */
    @Test
    public void writeOne() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_repeat.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, WritePerson.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "多次重复写入")
                    .build();
            //分批进行写入。实际上，是根据数据库的分页的总页数来确定。
            for (int i = 0; i < 5; i++) {
                //填入数据
                excelWriter.write(WriteDataUtil.getData(), writeSheet);
            }
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 写入到不同的sheet 里面。
     *
     * @throws Exception
     */
    @Test
    public void writeSheet() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_repeat2.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, WritePerson.class).build();
            //分批进行写入到不同的sheet页里面。 创建不同的WriteSheet 对象。
            for (int i = 0; i < 5; i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "多次重复写入" + i)
                        .build();
                //填入数据
                excelWriter.write(WriteDataUtil.getData(), writeSheet);
            }
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 写入到不同的sheet，不同的对象里面。
     *
     * @throws Exception
     */
    @Test
    public void writeSheetObj() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_repeat3.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream).build();
            //分批进行写入到不同的sheet页里面。 创建不同的WriteSheet 对象。
            for (int i = 0; i < 5; i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "多次重复写入" + i)
                        //每一个Sheet 页，指定不同的对象接收。
                        .head(WritePerson.class)
                        .build();
                //填入数据
                excelWriter.write(WriteDataUtil.getData(), writeSheet);
            }
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }
}
