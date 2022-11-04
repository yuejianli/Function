package top.yueshushu.easyexcel.writepojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName:WritePerson
 * @Description TODO
 * @Author yjl
 * @Date 2021/6/8 9:15
 * @Version 1.0
 **/
@Data
public class WritePersonComplex {
    /**
     * @param id id编号
     * @param name 名称
     * @param birthday 生日
     * @param age 年龄
     * @param score 成绩
     *
     */
    /**
     * 多复杂表头时，进行使用 {"主表头"，"副表头"}
     */
    @ExcelProperty(value = {"个人信息", "Id编号"})
    private Integer id;
    @ExcelProperty(value = {"个人信息", "名称"})
    private String name;
    @ExcelProperty(value = {"个人信息", "生日"})
    private Date birthday;
    @ExcelProperty(value = {"个人信息", "年龄"})
    private Integer age;
    @ExcelProperty(value = {"个人信息", "成绩"})
    private Double score;
    @ExcelProperty(value = "其他字段")
    private String other;

}
