package top.yueshushu.easyrule.test;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.easyrule.business.FieldCondition;
import top.yueshushu.easyrule.business.UserFieldChangeRule;
import top.yueshushu.easyrule.model.User;
import top.yueshushu.easyrule.model.UserStrategy;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-30
 */
@SpringBootTest
@Slf4j
public class BusinessTest {
	
	private List<User> userList;
	private UserStrategy userStrategy;
	
	@Before
	public void initData() {
		userList = new ArrayList<>();
		User user1 = new User();
		user1.setId(1);
		user1.setAge(24);
		user1.setSex("男");
		user1.setDescription("性别男,24岁");
		userList.add(user1);
		
		
		User user2 = new User();
		user2.setId(2);
		user2.setAge(17);
		user2.setSex("男");
		user2.setDescription("性别男,17岁");
		userList.add(user2);
		
		User user3 = new User();
		user3.setId(3);
		user3.setAge(24);
		user3.setSex("女");
		user3.setDescription("性别女,24岁");
		userList.add(user3);
		
		User user4 = new User();
		user4.setId(4);
		user4.setAge(17);
		user4.setSex("女");
		user4.setDescription("性别女,17岁");
		userList.add(user4);
		
		
		// 放置策略
		userStrategy = new UserStrategy();
		userStrategy.setId(1);
		userStrategy.setUserId(1);
		
	}
	
	
	@Test
	public void ruleTest() {
		
		String condition = "age,greater,18;sex,contain,男;";
		
		userList.forEach(
				user -> {
					//1. 构建 Facts
					Facts facts = new Facts();
					
					// 放置数据
					facts.put("user", user);
					facts.put("userStrategy", userStrategy);
					
					//2. 构建规则
					UserFieldChangeRule userFieldChangeRule = new UserFieldChangeRule();
					
					String[] conditionSplit = condition.split(";");
					
					for (String conditionSingle : conditionSplit) {
						// 放置信息
						// 添加条件， age > 18 并且是男性。
						FieldCondition fieldCondition = new FieldCondition(User.class);
						String[] filedNameSplit = conditionSingle.split(",");
						
						fieldCondition.setFieldName(filedNameSplit[0]);
						fieldCondition.setComparator(filedNameSplit[1]);
						fieldCondition.setValue(filedNameSplit[2]);
						userFieldChangeRule.addRule(fieldCondition);
					}
					
					Rules rules = new Rules();
					rules.register(userFieldChangeRule);
					// 3. 设置规则
					/**
					 skipOnFirstAppliedRule：告诉引擎规则被触发时跳过后面的规则。
					 skipOnFirstFailedRule：告诉引擎在规则失败时跳过后面的规则。
					 skipOnFirstNonTriggeredRule：告诉引擎一个规则不会被触发跳过后面的规则。
					 rulePriorityThreshold：告诉引擎如果优先级超过定义的阈值，则跳过下一个规则。版本3.3已经不支持更改，默认MaxInt。
					 */
					RulesEngineParameters parameters = new RulesEngineParameters()
							.skipOnFirstFailedRule(true);
					
					RulesEngine rulesEngine = new DefaultRulesEngine(parameters);
					
					rulesEngine.fire(rules, facts);
				}
		);
		
	}
	
}
