package top.yueshushu.mapstruct.map.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户展示Vo 层
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Data
public class MapUserVo implements Serializable {
	private Integer id;
	private String name;
	private Date birthday;
	private Integer age;
	private String sex;
	private String description;
	private String userPassword;
	
	//定义关联对象 学校
	private MapUserSchoolVo mapUserSchoolVo;
	
	
}
