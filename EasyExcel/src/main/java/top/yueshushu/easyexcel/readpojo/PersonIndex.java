package top.yueshushu.easyexcel.readpojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName:Person
 * @Description TODO
 * @Author yjl
 * @Date 2021/6/7 17:24
 * @Version 1.0
 **/
@Data
public class PersonIndex {
    /**
     * @param id id编号
     * @param name 名称
     * @param age 年龄
     * @param birthday 生日
     */
    /**
     * 添加 读取时的读取属性，按照 索引进行读取，按照名称进行读取。
     * 实际开发中，只用一种比较好。
     */
    @ExcelProperty(index = 0)
    private Integer id;
    @ExcelProperty(index = 1)
    private String name;
    @ExcelProperty(value = "年龄")
    private Integer age;
    @ExcelProperty(value = "生日")
    private Date birthday;
}
