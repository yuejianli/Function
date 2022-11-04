package top.yueshushu.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.ConvertWrite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:ConvertHeadF15
 * @Description 转换标题
 * @Author yjl
 * @Date 2021/6/8 9:24
 * @Version 1.0
 **/
@SpringBootTest
public class ConvertHeadF15 {
    @Test
    public void convertWrite() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_convertHead.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, ConvertWrite.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "转换").head(dynamicHead())
                    .build();
            //填入数据
            excelWriter.write(WriteDataUtil.getConvertData(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private List<List<String>> dynamicHead() {
        //里面放置两个 List ,主要可以用于复杂表头.
        List<List<String>> headerList = new ArrayList<>();
        List<String> header1 = new ArrayList<>();
        // 可以进行国际化处理
        header1.add("国际化:id编号");

        List<String> header2 = new ArrayList<>();
        header2.add("国际化:名称");

        List<String> header3 = new ArrayList<>();
        header3.add("国际化:生日");

        List<String> header4 = new ArrayList<>();
        header4.add("国际化:其他");
        headerList.add(header1);
        headerList.add(header2);
        headerList.add(header3);
        headerList.add(header4);
        return headerList;
    }
}
