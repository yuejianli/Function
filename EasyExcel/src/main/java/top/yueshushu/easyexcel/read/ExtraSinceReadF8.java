package top.yueshushu.easyexcel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import top.yueshushu.easyexcel.handler.ExtraSinceReadHandler;
import top.yueshushu.easyexcel.readpojo.ExtraSince;

import java.io.InputStream;

/**
 * @ClassName:extraRead
 * @Description 额外信息（批注、超链接、合并单元格信息读取）
 * @Author yjl
 * @Date 2021/6/7 20:10
 * @Version 1.0
 **/
@SpringBootTest
public class ExtraSinceReadF8 {
    @Test
    public void extraReadTest() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("easyexcelsince.xlsx");
        InputStream inputStream = classPathResource.getInputStream();
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(inputStream, ExtraSince.class, new ExtraSinceReadHandler())
                    //需要读取批注
                    .extraRead(CellExtraTypeEnum.COMMENT)
                    //读取超链接
                    .extraRead(CellExtraTypeEnum.HYPERLINK)
                    //读取合并单元格
                    .extraRead(CellExtraTypeEnum.MERGE)
                    .build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                //需要关闭
                excelReader.finish();
            }
        }
    }
}
