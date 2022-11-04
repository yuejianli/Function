package top.yueshushu.easyexcel.writepojo;

import com.alibaba.excel.annotation.ExcelIgnore;
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
public class WritePerson {
    /**
     * @param id id编号
     * @param name 名称
     * @param birthday 生日
     * @param age 年龄
     * @param score 成绩
     *
     */
    //写入时，如果不指定 value，即列的名称的话，那么会用类属性的名称进行替代。
    /**
     * 不指定 index 时，则按照从左到右的顺序，依次插入。
     * 如果指定了 index的话，则以 index为主，进行插入，可以跳列。
     */
    @ExcelProperty(value = "Id编号")
    private Integer id;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "生日")
    private Date birthday;
    @ExcelProperty(value = "年龄")
    private Integer age;
    @ExcelProperty(value = "成绩")
    private Double score;
    @ExcelProperty(value = "成绩2")
    private Double score2;
    //忽略的字段
    @ExcelIgnore
    private String other;
}
