package top.yueshushu.lombok.simple.nouse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  setting 和 getting 注解
 *
 * @author yuejianli
 * @date 2022-07-07
 */
//@ToString
@ToString(of = {"id","name"},exclude = {"description","MIN_AGE"})
public class UserUseToString {
	// 属性
	private Integer id;
	private String name;
	private String description;
	private final Integer MIN_AGE = 18;
}
