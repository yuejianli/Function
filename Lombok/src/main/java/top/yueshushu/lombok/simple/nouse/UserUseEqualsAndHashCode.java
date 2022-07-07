package top.yueshushu.lombok.simple.nouse;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *  setting 和 getting 注解
 *
 * @author yuejianli
 * @date 2022-07-07
 */
//@EqualsAndHashCode
@EqualsAndHashCode(of = {"name"},exclude = {"id","description"})
public class UserUseEqualsAndHashCode {
	// 属性
	private Integer id;
	private String name;
	private String description;
}
