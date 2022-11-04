package top.yueshushu.easyexcel.writepojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.Data;
import top.yueshushu.easyexcel.handler.CustomStringConverter;

import java.util.Date;

/**
 * @ClassName:ConvertWrite
 * @Description TODO
 * @Author yjl
 * @Date 2021/6/8 10:43
 * @Version 1.0
 **/
@Data
public class ConvertWrite {
    /**
     * @param id id编号
     * @param name 名称
     * @param birthday 生日
     * @param age 年龄
     * @param score 成绩
     */
    @ExcelProperty(value = "Id编号")
    private Integer id;
    //进行转换
    @ExcelProperty(value = "名称", converter = CustomStringConverter.class)
    private String name;
    @ExcelProperty(value = "生日")
    @DateTimeFormat(value = "yyyy年MM月dd日")
    private Date birthday;
    @ExcelProperty(value = "年龄")
    private Integer age;
    @ExcelProperty(value = "成绩")
    @NumberFormat(value = "#.##%")
    private Double score;
    //忽略的字段
    @ExcelIgnore
    private String other;
}
