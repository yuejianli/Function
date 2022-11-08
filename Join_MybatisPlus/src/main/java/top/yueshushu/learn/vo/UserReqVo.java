package top.yueshushu.learn.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-11-08
 */
@Data
public class UserReqVo extends BaseEntityReqVo implements Serializable {
    // 用户的条件 可以多个
    private String name;
    // 角色的条件 较少
    private String roleName;
}
