package top.yueshushu.mapstruct.map.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户转换中间层
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Data
public class MapUser implements Serializable {
	private Integer id;
	private String name;
	private Date birthday;
	private Integer age;
	private Integer sex;
	private String description;
	private String password;
	// 定义关联对象 School
	private MapUserSchool mapUserSchool;
}
