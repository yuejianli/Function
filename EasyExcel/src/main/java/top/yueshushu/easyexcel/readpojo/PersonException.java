package top.yueshushu.easyexcel.readpojo;

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
public class PersonException {
    /**
     * @param id id编号
     * @param name 名称
     * @param age 年龄
     * @param birthday 生日
     */
    private Integer id;
    private String name;
    //用age 去接收字母，会出错的。
    private Integer age;
    private Date birthday;
}
