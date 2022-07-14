package top.yueshushu.mapstruct.map.assember;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Component
public class UserSexTransform {
	
	public String integerToString(Integer sex) {
		if (sex == null) {
			return "男";
		}
		return sex == 1 ? "男" : "女";
	}
	
	public Integer stringToInteger(String sex) {
		if (StringUtils.isEmpty(sex)) {
			return 1;
		}
		return "男".equals(sex) ? 1 : 0;
	}
}
