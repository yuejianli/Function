package top.yueshushu.easyexcel.readpojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.data.CellData;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName:Formula
 * @Description 读取公式时使用
 * @Author yjl
 * @Date 2021/6/7 20:15
 * @Version 1.0
 **/
@Data
public class Formula {
    @ExcelProperty(index = 0)
    private CellData<Integer> ids;
    @ExcelProperty(index = 1)
    private CellData<String> name;
    @ExcelProperty(index = 2)
    private CellData<Date> birthday;
    @ExcelProperty(index = 3)
    private CellData<String> formulaValue;
}
