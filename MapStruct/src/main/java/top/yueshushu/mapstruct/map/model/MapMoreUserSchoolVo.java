package top.yueshushu.mapstruct.map.model;

import java.util.Date;

import lombok.Data;

/**
 * 多对象合并成一个处理
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Data
public class MapMoreUserSchoolVo {
	// 用户对象
	private Integer id;
	private String name;
	private Date birthday;
	private Integer age;
	private String sex;
	private String description;
	private String userPassword;
	
	// 学校对象
	private Integer schoolId;
	private String schoolName;
	private String schoolAddress;
}
