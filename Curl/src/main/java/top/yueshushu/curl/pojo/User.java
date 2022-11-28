package top.yueshushu.curl.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-11-28
 */
@Data
public class User implements Serializable {
    private String name;
    private String sex;
}
