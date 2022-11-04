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
 * @ClassName:ColumnWrite
 * @Description 导出时，只导出某些列，或者排除某些列
 * @Author yjl
 * @Date 2021/6/8 10:05
 * @Version 1.0
 **/
@SpringBootTest
public class HideColumnWriteF2 {

    /**
     * 导出时，排除某些列
     */
    @Test
    public void writeExclude() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_exclude.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            List<String> excludeColumnList = new ArrayList<>();
            excludeColumnList.add("name");
            excludeColumnList.add("age");
            excelWriter = EasyExcel.write(outputStream, WritePerson.class)
                    //去掉某些列
                    .excludeColumnFieldNames(excludeColumnList)
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet("去掉某些列")
                    .build();
            //放置数据
            excelWriter.write(WriteDataUtil.getData(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 导出时，只导出某些列
     */
    @Test
    public void writeInclude() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_include.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            List<String> includeCloumnList = new ArrayList<>();
            includeCloumnList.add("name");
            includeCloumnList.add("age");
            excelWriter = EasyExcel.write(outputStream, WritePerson.class)
                    //只写入某些列
                    .includeColumnFieldNames(includeCloumnList)
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet("只包括列").build();
            excelWriter.write(WriteDataUtil.getData(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }


}
