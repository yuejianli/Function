package top.yueshushu.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.property.OnceAbsoluteMergeProperty;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.MergePerson;
import top.yueshushu.easyexcel.writepojo.WritePerson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @ClassName:FormatWrite
 * @Description 合并
 * @Author yjl
 * @Date 2021/6/8 10:59
 * @Version 1.0
 **/
@SpringBootTest
public class MergeWriteF10 {
    @Test
    public void mergeWrite() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_merge.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream, MergePerson.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "格式化列宽和行高")
                    .build();
            //填入数据
            excelWriter.write(WriteDataUtil.getMergeData(), writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    @Test
    public void mergeWrite2() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "write_merge2.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(2, 0);
            OnceAbsoluteMergeProperty onceAbsoluteMergeProperty = new OnceAbsoluteMergeProperty(3, 4, 3, 4);
            OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy = new OnceAbsoluteMergeStrategy(onceAbsoluteMergeProperty);
            excelWriter = EasyExcel.write(outputStream, WritePerson.class)
                    .registerWriteHandler(loopMergeStrategy)
                    .registerWriteHandler(onceAbsoluteMergeStrategy)
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "格式化列宽和行高")
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
