package top.yueshushu.model;

import lombok.Data;
import top.yueshushu.annotation.TimeZoneField;
import top.yueshushu.entity.TimeRange;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yue Jianli
 * @date 2022-05-18
 */
@Data
public class UserRo implements Serializable {
    private String name;
    private String sex;
    private Integer age;
    private String description;
    @TimeZoneField
    private List<String> timeRange;
}
