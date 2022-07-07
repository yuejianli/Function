package top.yueshushu.lombok.simple.nouse;

import lombok.Builder;
import lombok.Data;

/**
 * @Data 注解
 *
 * @author yuejianli
 * @date 2022-07-07
 */
@Data
@Builder
public class UserUseBuilder {
	
	// 属性
	private Integer id;
	private String name;
	private String description;
}
