package top.yueshushu.mapstruct.spring.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Data
public class SpringUserVo implements Serializable {
	private Integer id;
	private String name;
	private Date birthday;
	private Integer age;
	private String sex;
	private String description;
	private String userPassword;
}
