package top.yueshushu.easyexcel.readpojo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName:Person
 * @Description 普通的读取和写入使用
 * @Author yjl
 * @Date 2021/6/7 17:24
 * @Version 1.0
 **/
@Data
public class Person {
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
