package top.yueshushu.easyrule.test;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.easyrule.model.RuleItem;

/**
 * 以前的处理方式
 *
 * @author yuejianli
 * @date 2022-06-29
 */
// @SpringBootTest
@Slf4j
public class EasyRuleTest {
	
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
	
	/**
	 * 一个条件规则
	 */
	@Test
	public void barcodeATest() {
		
		itemList.forEach(
				n -> {
					showSimplePrice(n);
					log.info("show {} showSimplePrice is {}", n.getBarcode(), n.getShowPrice());
				}
		);
	}
	
	
	/**
	 * 原先的条件判断，处理商品的展示信息
	 * 条码多个规则处理
	 */
	@Test
	public void barcodeABCTest() {
		
		itemList.forEach(
				n -> {
					showBarcodePrice(n);
					log.info("show {} barcodeABC is {}", n.getBarcode(), n.getShowPrice());
				}
		);
	}
	
	
	/**
	 * 多规则联合处理
	 */
	@Test
	public void moreTest() {
		
		itemList.forEach(
				n -> {
					showMorePrice(n);
					log.info("show {} showMorePrice is {}", n.getBarcode(), n.getShowPrice());
				}
		);
	}
	
	/**
	 * 多规则处理
	 *
	 * @param ruleItem 规则商品
	 */
	private void showBarcodePrice(RuleItem ruleItem) {
		//1. 定义事实
		Facts facts = new Facts();
		facts.put("item", ruleItem);
		
		//2. 创建规则，并注册到 规则集合里面.
		Rules rules = new Rules();
		
		Rule rule1 = new RuleBuilder()
				.name("code A")
				.description("code A的规则")
				.priority(1)
				.when(fa -> ((RuleItem) facts.get("item")).getBarcode().startsWith("A"))
				.then(fa -> ((RuleItem) facts.get("item")).setShowPrice(new BigDecimal("100")))
				.build();
		
		Rule rule2 = new RuleBuilder()
				.name("code B")
				.description("code B的规则")
				.priority(2)
				.when(fa -> ((RuleItem) facts.get("item")).getBarcode().startsWith("B"))
				.then(fa -> ((RuleItem) facts.get("item")).setShowPrice(new BigDecimal("200")))
				.build();
		
		Rule rule3 = new RuleBuilder()
				.name("code C")
				.description("code C 的规则")
				.priority(3)
				.when(fa -> ((RuleItem) facts.get("item")).getBarcode().startsWith("C") && (((RuleItem) facts.get("item")).getDay() > 20))
				.then(fa -> ((RuleItem) facts.get("item")).setShowPrice(new BigDecimal("100")))
				.build();
		
		// 添加规则
		rules.register(rule1);
		rules.register(rule2);
		rules.register(rule3);
		
		//3. 定义执行引擎
		RulesEngine rulesEngine = new DefaultRulesEngine();
		
		rulesEngine.fire(rules, facts);
	}
	
	
	/**
	 * 一个简单的规则处理
	 *
	 * @param ruleItem 商品规则
	 */
	private void showSimplePrice(RuleItem ruleItem) {
		
		//1. 定义事实, 事实里面放置相应的数据。
		Facts facts = new Facts();
		
		facts.put("ruleItem", ruleItem);
		
		// 2. 创建规则
		Rules rules = new Rules();
		
		Rule rule = new RuleBuilder()
				.name("ruleItem rule")
				.description("商品规则 条码A ")
				.priority(1)
				// 定义优化级
				.when(fa -> ((RuleItem) (facts.get("ruleItem"))).getBarcode().startsWith("A"))
				.then(fa -> ((RuleItem) (facts.get("ruleItem"))).setShowPrice(new BigDecimal("100")))
				.build();
		// 3. 注册规则到规则组里面
		rules.register(rule);
		
		// 4. 定义规则引擎
		RulesEngine rulesEngine = new DefaultRulesEngine();
		
		//5. 执行
		rulesEngine.fire(rules, facts);
		
	}
	
	/**
	 * 多规则条件
	 */
	private void showMorePrice(RuleItem ruleItem) {
		//1. 定义 Facts, 并填充数据.
		Facts facts = new Facts();
		facts.put("item", ruleItem);
		
		//2. 定义规则
		Rule rule1 = new RuleBuilder()
				.name("item barcodeRule 1")
				.description("规则 条码为A")
				.priority(1)
				.when(fa -> ((RuleItem) facts.get("item")).getBarcode().startsWith("A"))
				.then(fa -> ((RuleItem) facts.get("item")).setShowPrice(new BigDecimal("100")))
				.build();
		
		Rule rule2 = new RuleBuilder()
				.name("item barcode Rule 2")
				.description(" 规则 条码为B")
				.priority(2)
				.when(fa -> ((RuleItem) facts.get("item")).getBarcode().startsWith("B"))
				.then(fa -> ((RuleItem) facts.get("item")).setShowPrice(new BigDecimal("200")))
				.build();
		Rule rule3 = new RuleBuilder()
				.name("item barcode Rule 3")
				.description(" 规则 条码为C")
				.priority(3)
				.when(fa -> ((RuleItem) facts.get("item")).getBarcode().startsWith("C") && ((RuleItem) facts.get("item")).getDay() > 30)
				.then(fa -> ((RuleItem) facts.get("item")).setShowPrice(
						NumberUtil.mul(((RuleItem) facts.get("item")).getOriginPrice(), new BigDecimal("0.5")
						)))
				.build();
		
		Rule rule4 = new RuleBuilder()
				.name("item barcode Rule 3")
				.description(" 规则 条码为C")
				.priority(4)
				.when(fa -> ((RuleItem) facts.get("item")).getBarcode().startsWith("C") && ((RuleItem) facts.get("item")).getDay() > 20 && ((RuleItem) facts.get("item")).getDay() <= 30)
				.then(fa -> ((RuleItem) facts.get("item")).setShowPrice(
						NumberUtil.mul(((RuleItem) facts.get("item")).getOriginPrice(), new BigDecimal("0.8")
						)))
				.build();
		
		
		Rule rule5 = new RuleBuilder()
				.name("item barcode Rule 3")
				.description(" 规则 条码为C")
				.priority(5)
				.when(fa -> ((RuleItem) facts.get("item")).getBarcode().startsWith("C") && ((RuleItem) facts.get("item")).getDay() <= 20)
				.then(fa -> ((RuleItem) facts.get("item")).setShowPrice(
						NumberUtil.mul(((RuleItem) facts.get("item")).getOriginPrice(), new BigDecimal("1.0")
						)))
				.build();
		// 添加到规则集合里面
		Rules rules = new Rules();
		rules.register(rule1);
		rules.register(rule2);
		rules.register(rule3);
		rules.register(rule4);
		rules.register(rule5);
		
		//3. 定义默认执行器
		RulesEngine rulesEngine = new DefaultRulesEngine();
		
		//4. 执行操作, 关联上规则集合和条件。
		rulesEngine.fire(rules, facts);
		
	}
	
}
