package top.yueshushu.model;

import lombok.Data;
import top.yueshushu.annotation.TimeZoneField;

import java.io.Serializable;

/**
 * 员工Vo
 *
 * @author Yue Jianli
 * @date 2022-05-18
 */
@Data
public class UserVo implements Serializable {
    private Integer id;
    private String name;
    private String sex;
    private Integer age;
    private String description;

    private String country;
    /**
     * 处理插入时间和更新时间
     */
    private String createTime;
    @TimeZoneField
    private String updateTime;

    /**
     * 处理插入时间和更新时间
     */
    private Long createTimeNum;

    private Long updateTimeNum;
}
