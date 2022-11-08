package top.yueshushu.learn.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-11-08
 */
@Data
public class UserDo implements Serializable {

    /**
     * @param id id编号
     * @param name 姓名
     * @param sex 性别
     * @param age 年龄
     * @param description 描述
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "sex")
    private String sex;
    @TableField(value = "age")
    private Integer age;
    @TableField(value = "role_id")
    private Integer roleId;
    @TableField(value = "description")
    private String description;

    @TableField(value = "role_name")
    private String roleName;
    @TableField(value = "role_description")
    private String roleDescription;
}
