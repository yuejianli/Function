package top.yueshushu.easyexcel.fill;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
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
 * @ClassName:ComplexFillWriterF4
 * @Description 复杂填充
 * @Author yjl
 * @Date 2021/6/9 17:36
 * @Version 1.0
 **/
@SpringBootTest
public class ComplexFillWriterF4 {
    /**
     * 横向填入
     *
     * @throws Exception
     */
    @Test
    public void listFill3Test() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "fill_template_sum4.xlsx");
        InputStream tempInputStream = new FileInputStream(file);
        //进行填充.
        File objFile = new File(WriteDataUtil.FILE_PATH + "fill_sum4.xlsx");
        OutputStream objOutputStream = new FileOutputStream(objFile);
        List<WritePerson> personList = WriteDataUtil.getData();
        //进行配置


        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(objOutputStream).withTemplate(tempInputStream).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 data1，然后多个list必须用 FillWrapper包裹
            excelWriter.fill(new FillWrapper("data1", personList), fillConfig, writeSheet);
            excelWriter.fill(new FillWrapper("data1", personList), fillConfig, writeSheet);
            excelWriter.fill(new FillWrapper("data2", personList), writeSheet);
            excelWriter.fill(new FillWrapper("data2", personList), writeSheet);
            excelWriter.fill(new FillWrapper("data3", personList), writeSheet);
            excelWriter.fill(new FillWrapper("data3", personList), writeSheet);

            Map<String, Object> map = new HashMap<String, Object>();
            //map.put("date", "2019年10月9日13:28:28");
            map.put("date", new Date());

            excelWriter.fill(map, writeSheet);
        } finally {
            // 千万别忘记关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }
}
