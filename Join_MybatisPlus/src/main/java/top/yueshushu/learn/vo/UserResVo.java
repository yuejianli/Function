package top.yueshushu.learn.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户响应信息
 *
 * @author yuejianli
 * @date 2022-11-08
 */
@Data
@Accessors(chain = true)
// 为空时，不进行序列化
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResVo implements Serializable {
    private static final long serialVersionUID = -1L;
    // 响应的用户信息
    private Integer id;
    private String name;
    private String sex;
    private Integer age;
    private Integer roleId;
    private String description;
    // 响应的角色信息
    private String roleName;
    private String roleDescription;
}
