package top.yueshushu.easyexcel.fill;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.easyexcel.util.WriteDataUtil;
import top.yueshushu.easyexcel.writepojo.WritePerson;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:CrossFillWriterF4
 * @Description 横向填充
 * @Author yjl
 * @Date 2021/6/9 17:36
 * @Version 1.0
 **/
@SpringBootTest
public class CrossFillWriterF4 {
    /**
     * 横向填入
     *
     * @throws Exception
     */
    @Test
    public void listFill3Test() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "fill_template_sum3.xlsx");
        InputStream tempInputStream = new FileInputStream(file);
        //进行填充.
        File objFile = new File(WriteDataUtil.FILE_PATH + "fill_sum3.xlsx");
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
                .direction(WriteDirectionEnum.HORIZONTAL)
                .build();
        //进行填充数据
        excelWriter.fill(personList, fillConfig, writeSheet);
        excelWriter.fill(personList, fillConfig, writeSheet);
        //用Map 填充其他的值,根据参数。
        Map<String, Object> map = new HashMap<>();
        map.put("date", new Date());
        excelWriter.fill(map, writeSheet);
        excelWriter.finish();
    }
}
