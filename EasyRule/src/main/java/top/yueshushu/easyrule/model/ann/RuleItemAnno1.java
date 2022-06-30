package top.yueshushu.easyrule.model.ann;


import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import java.math.BigDecimal;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.easyrule.model.RuleItem;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-29
 */
@Data
// 添加 @Rule 注解， 指定这一个是规则
@Rule(name = "barcode A", description = "barCode A的规则")
@Slf4j
public class RuleItemAnno1 {
	
	// @Condition 指定条件
	@Condition
	public boolean isConform(@Fact("item") RuleItem ruleItem) {
		return ruleItem.getBarcode().startsWith("A");
	}
	
	// @Action  注解，指定 条件符合后，要执行的动作.
	@Action
	public void startAAction(@Fact("item") RuleItem ruleItem) {
		ruleItem.setShowPrice(new BigDecimal("100"));
	}
}
