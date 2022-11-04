package top.yueshushu.easyexcel.fillpojo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName:FillPerson
 * @Description 填充数据使用的 对象
 * @Author yjl
 * @Date 2021/6/9 9:48
 * @Version 1.0
 **/
@Data
public class FillPerson {
    /**
     * @param id id编号
     * @param name 名称
     * @param age 年龄
     * @param birthday 生日
     */
    private Integer id;
    private String name;
    private Integer age;
    private Date birthday;
}
