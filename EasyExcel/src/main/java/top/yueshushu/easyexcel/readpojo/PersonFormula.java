package top.yueshushu.easyexcel.readpojo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName:Person
 * @Description 公式
 * @Author 公式
 * @Date 2021/6/7 17:24
 * @Version 1.0
 **/
@Data
public class PersonFormula {
    /**
     * @param id id编号
     * @param name 名称
     * @param age 年龄
     * @param birthday 生日
     */
    private Integer id;
    private String name;
    private Date birthday;
    private String desc;
}
