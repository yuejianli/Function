package top.yueshushu.lombok.simple.nouse;

import lombok.*;

/**
 *  setting 和 getting 注解
 *
 * @author yuejianli
 * @date 2022-07-07
 */
@Data
public class UserUseNonNull {
	// 属性
	private Integer id;
	@NonNull
	private String name;
	private String description;
}
