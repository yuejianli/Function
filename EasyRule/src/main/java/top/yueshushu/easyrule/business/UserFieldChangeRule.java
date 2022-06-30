package top.yueshushu.easyrule.business;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.easyrule.model.User;
import top.yueshushu.easyrule.model.UserStrategy;
import top.yueshushu.easyrule.util.EngineUtil;

/**
 * 用户属性规则
 *
 * @author yuejianli
 * @date 2022-06-30
 */
@Rule(name = "user field rule", description = "用户属性规则")
@Slf4j
public class UserFieldChangeRule {
	
	private List<FieldCondition<User>> userConditionList = new ArrayList<>();
	
	@Condition
	public boolean ruleValidation(@Fact("user") User user, @Fact("userStrategy") UserStrategy userStrategy) {
		if (CollectionUtils.isEmpty(userConditionList)) {
			return false;
		}
		// 对条件进行解析，执行.
		log.info("user info :{}", user);
		log.info("strategy : {}", userStrategy);
		for (FieldCondition fieldCondition : userConditionList) {
			Field field = fieldCondition.getField();
			if (field == null) {
				continue;
			}
			try {
				field.setAccessible(true);
				Object value = field.get(user);
				String valueInString = value == null ? "null" : value.toString();
				
				String fieldPart = fieldCondition.getFieldPart();
				if (fieldPart != null && !valueInString.equals("null")) {
					String[] values = valueInString.split("\\.");
					if (fieldPart.equals("Integer")) {
						valueInString = values[0];
					} else if (fieldPart.equals("Decimal")) {
						valueInString = values[1];
					}
				}
				
				boolean conResult = EngineUtil.eval(fieldCondition.getComparator(), valueInString, fieldCondition.getValue(), field.getType());
				if (!conResult) {
					return false;
				}
			} catch (IllegalAccessException e) {
				continue;
			}
		}
		return true;
	}
	
	@Action
	public void execRule(@Fact("user") User user, @Fact("userStrategy") UserStrategy userStrategy) {
		// 对条件进行解析，执行. 执行成功的，就一个。
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>执行成功:user info :{}", user);
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>执行成功: strategy : {}", userStrategy);
	}
	
	/**
	 * 添加条件规则
	 *
	 * @param fieldCondition 条件规则
	 */
	public void addRule(FieldCondition fieldCondition) {
		userConditionList.add(fieldCondition);
	}
	
}
