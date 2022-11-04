package top.yueshushu.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.WritePersonComplex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @ClassName:WriteComplex
 * @Description 复杂头使用
 * @Author yjl
 * @Date 2021/6/8 10:23
 * @Version 1.0
 **/
@SpringBootTest
public class WriteComplexF3 {
    @Test
    public void complexTest() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_complex.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        EasyExcel.write(outputStream, WritePersonComplex.class)
                .sheet("表头合并").doWrite(WriteDataUtil.getData());
    }
}
