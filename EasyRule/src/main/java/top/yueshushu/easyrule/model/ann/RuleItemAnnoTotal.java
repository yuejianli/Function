package top.yueshushu.easyrule.model.ann;


import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

import cn.hutool.core.util.NumberUtil;
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
@Rule(name = "total Rule", description = "total 的规则")
@Slf4j
public class RuleItemAnnoTotal {
	
	// @Condition 指定条件
	@Condition
	public boolean isConform(@Fact("item") RuleItem ruleItem, @Fact("day") int day, @Fact("barcode") String barcode) {
		return true;
	}
	
	// @Action  注解，指定 条件符合后，要执行的动作.
	@Action
	public void startAAction(@Fact("item") RuleItem ruleItem, @Fact("day") int day, @Fact("barcode") String barcode) {
		if (StringUtils.isEmpty(barcode)) {
			return;
		}
		if (barcode.startsWith("A")) {
			ruleItem.setShowPrice(new BigDecimal("100"));
		} else if (barcode.startsWith("B")) {
			ruleItem.setShowPrice(new BigDecimal("200"));
		} else {
			if (day > 30) {
				ruleItem.setShowPrice(NumberUtil.mul(ruleItem.getOriginPrice(), new BigDecimal("0.5")));
			} else if (day > 20) {
				ruleItem.setShowPrice(NumberUtil.mul(ruleItem.getOriginPrice(), new BigDecimal("0.8")));
			} else {
				ruleItem.setShowPrice(NumberUtil.mul(ruleItem.getOriginPrice(), new BigDecimal("1.0")));
			}
		}
	}
}
