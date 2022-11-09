package top.yueshushu.learn.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-11-08
 */
@Data
public class UserDto implements Serializable {
    private Integer id;
    private String name;
    private String sex;
    private Integer age;
    private Integer roleId;
    private String description;
    private String roleName;
    private String roleDescription;
}
