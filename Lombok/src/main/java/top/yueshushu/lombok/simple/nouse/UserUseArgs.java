package top.yueshushu.lombok.simple.nouse;

import lombok.*;

/**
 * @Data 注解
 *
 * @author yuejianli
 * @date 2022-07-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UserUseArgs {
	
	// 属性
	private Integer id;
	@NonNull
	private String name;
	private String description;
	private final Integer MIN_AGE = 18;
}
