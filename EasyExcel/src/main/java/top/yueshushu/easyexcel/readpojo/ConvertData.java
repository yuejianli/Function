package top.yueshushu.easyexcel.readpojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.Data;
import top.yueshushu.easyexcel.handler.CustomStringConverter;

/**
 * @ClassName:ConvertData
 * @Description TODO
 * @Author yjl
 * @Date 2021/6/7 19:14
 * @Version 1.0
 **/
@Data
public class ConvertData {
    /**
     * @param id id编号
     * @param name 名称
     * @param birthday 生日
     * @param score 成绩
     */
    @ExcelProperty(index = 0)
    private Integer id;
    @ExcelProperty(index = 1, converter = CustomStringConverter.class)
    private String name;
    @ExcelProperty(index = 2)
    @DateTimeFormat("yyyy年MM月dd日")
    private String birthday;
    @NumberFormat("#.##%")
    @ExcelProperty(index = 3)
    private String score;
}
