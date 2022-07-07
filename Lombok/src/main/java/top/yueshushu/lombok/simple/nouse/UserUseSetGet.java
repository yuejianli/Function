package top.yueshushu.lombok.simple.nouse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 *  setting 和 getting 注解
 *
 * @author yuejianli
 * @date 2022-07-07
 */

public class UserUseSetGet {
	// 属性
	@Setter
	@Getter
	private Integer id;
	@Setter(value= AccessLevel.PROTECTED)
	@Getter(value= AccessLevel.PROTECTED)
	private String name;
	@Setter(value = AccessLevel.PRIVATE)
	@Getter(value = AccessLevel.PRIVATE)
	private String description;
	
	@Setter
	@Getter
	private final Integer MIN_AGE = 18;
	
}
