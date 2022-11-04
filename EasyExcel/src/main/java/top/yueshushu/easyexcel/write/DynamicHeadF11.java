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
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:DynamicHeadF11
 * @Description 动态头写入
 * @Author yjl
 * @Date 2021/6/8 10:59
 * @Version 1.0
 **/
@SpringBootTest
public class DynamicHeadF11 {
    @Test
    public void dynamicWrite() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_dynamic.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, WritePerson.class)
                    .head(header())
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "动态表头")
                    .build();
            //填入数据
            excelWriter.write(WriteDataUtil.getData(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private List<List<String>> header() {
        //里面放置两个 List ,主要可以用于复杂表头.
        List<List<String>> headerList = new ArrayList<>();
        List<String> header1 = new ArrayList<>();
        header1.add("个人信息");
        header1.add("id编号");

        List<String> header2 = new ArrayList<>();
        header2.add("个人信息");
        header2.add("名称");

        List<String> header3 = new ArrayList<>();
        header3.add("个人信息");
        header3.add("生日");

        List<String> header4 = new ArrayList<>();
        header4.add("其他");
        headerList.add(header1);
        headerList.add(header2);
        headerList.add(header3);
        headerList.add(header4);
        return headerList;
    }
}
