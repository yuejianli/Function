package top.yueshushu.easyrule.test;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.easyrule.model.RuleItem;
import top.yueshushu.easyrule.model.ann.RuleItemAnno1;
import top.yueshushu.easyrule.model.ann.RuleItemAnno2;
import top.yueshushu.easyrule.model.ann.RuleItemAnno3;
import top.yueshushu.easyrule.model.ann.RuleItemAnnoTotal;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-29
 */
@Slf4j
public class RuleAnnoTest {
	
	private List<RuleItem> itemList;
	
	@Before
	public void initRuleItemList() {
		itemList = new ArrayList<>();
		RuleItem item1 = new RuleItem();
		item1.setBarcode("A0001");
		item1.setTitle("A0001商品");
		item1.setDay(10);
		item1.setOriginPrice(new BigDecimal("10"));
		itemList.add(item1);
		
		RuleItem item2 = new RuleItem();
		item2.setBarcode("B0002");
		item2.setTitle("B0002商品");
		item2.setDay(20);
		item2.setOriginPrice(new BigDecimal("20"));
		itemList.add(item2);
		
		RuleItem item3 = new RuleItem();
		item3.setBarcode("C0003");
		item3.setTitle("C0003 商品");
		item3.setDay(31);
		item3.setOriginPrice(new BigDecimal("30"));
		
		itemList.add(item3);
		
		RuleItem item4 = new RuleItem();
		item4.setBarcode("C0004");
		item4.setTitle("C0004 商品");
		item4.setDay(21);
		item4.setOriginPrice(new BigDecimal("30"));
		
		itemList.add(item4);
		
		
		RuleItem item5 = new RuleItem();
		item5.setBarcode("C0005");
		item5.setTitle("C0005 商品");
		item5.setDay(9);
		item5.setOriginPrice(new BigDecimal("30"));
		
		itemList.add(item5);
		
	}
	
	@Test
	public void annoTest() {
		itemList.forEach(
				n -> {
					annoShowPrice(n);
					log.info("show {} showSimplePrice is {}", n.getBarcode(), n.getShowPrice());
				}
		);
		
		
	}
	
	/**
	 * 注解形式处理
	 *
	 * @param ruleItem 规则商品
	 */
	private void annoShowPrice(RuleItem ruleItem) {
		
		// 规则处理
		Facts facts = new Facts();
		facts.put("item", ruleItem);
		
		Rules rules = new Rules();
		rules.register(new RuleItemAnno1());
		rules.register(new RuleItemAnno2());
		rules.register(new RuleItemAnno3());
		// 执行规则
		RulesEngine rulesEngine = new DefaultRulesEngine();
		rulesEngine.fire(rules, facts);
	}
	
	@Test
	public void annoAllTest() {
		itemList.forEach(
				n -> {
					annoAllShowPrice(n);
					log.info("show {} showSimplePrice is {}", n.getBarcode(), n.getShowPrice());
				}
		);
	}
	
	/**
	 * 注解形式处理
	 *
	 * @param ruleItem 规则商品
	 */
	private void annoAllShowPrice(RuleItem ruleItem) {
		
		// 规则处理
		Facts facts = new Facts();
		facts.put("item", ruleItem);
		facts.put("barcode", ruleItem.getBarcode());
		facts.put("day", ruleItem.getDay());
		
		Rules rules = new Rules();
		rules.register(new RuleItemAnnoTotal());
		// 执行规则
		RulesEngine rulesEngine = new DefaultRulesEngine();
		rulesEngine.fire(rules, facts);
	}
	
	/**
	 * 规则跳过处理
	 */
	
	
	@Test
	public void annoParamAllTest() {
		itemList.forEach(
				n -> {
					annoAllParamShowPrice(n);
					log.info("show {} showSimplePrice is {}", n.getBarcode(), n.getShowPrice());
				}
		);
	}
	
	/**
	 * 注解形式处理
	 *
	 * @param ruleItem 规则商品
	 */
	private void annoAllParamShowPrice(RuleItem ruleItem) {
		
		// 规则处理
		Facts facts = new Facts();
		facts.put("item", ruleItem);
		facts.put("barcode", ruleItem.getBarcode());
		facts.put("day", ruleItem.getDay());
		
		Rules rules = new Rules();
		rules.register(new RuleItemAnnoTotal());
		// 执行规则
		
		/**
		 skipOnFirstAppliedRule：告诉引擎规则被触发时跳过后面的规则。
		 skipOnFirstFailedRule：告诉引擎在规则失败时跳过后面的规则。
		 skipOnFirstNonTriggeredRule：告诉引擎一个规则不会被触发跳过后面的规则。
		 rulePriorityThreshold：告诉引擎如果优先级超过定义的阈值，则跳过下一个规则。版本3.3已经不支持更改，默认MaxInt。
		 */
		RulesEngineParameters parameters = new RulesEngineParameters()
				.skipOnFirstAppliedRule(false)
				.skipOnFirstFailedRule(true)
				.skipOnFirstNonTriggeredRule(true);
		
		RulesEngine rulesEngine = new DefaultRulesEngine(parameters);
		rulesEngine.fire(rules, facts);
	}
}
