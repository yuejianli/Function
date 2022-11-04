package top.yueshushu.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.handler.CommentWriteHandler;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.WritePerson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @ClassName:CommentHandlerF14
 * @Description 批注
 * @Author yjl
 * @Date 2021/6/8 10:59
 * @Version 1.0
 **/
@SpringBootTest
public class CommentHandlerF14 {
    @Test
    public void hrefWrite() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_comment.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, WritePerson.class)
                    .inMemory(Boolean.TRUE)
                    .registerWriteHandler(new CommentWriteHandler())
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "填入超链接")
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
