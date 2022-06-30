package top.yueshushu.easyrule.test;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.easyrule.model.Item;

/**
 * 以前的处理方式
 *
 * @author yuejianli
 * @date 2022-06-29
 */
// @SpringBootTest
@Slf4j
public class OldDemoTest {
	
	private List<Item> itemList;
	
	@Before
	public void initItemList() {
		itemList = new ArrayList<>();
		Item item1 = new Item();
		item1.setBarcode("A0001");
		item1.setTitle("A0001商品");
		item1.setDay(10);
		item1.setOriginPrice(new BigDecimal("10"));
		itemList.add(item1);
		
		Item item2 = new Item();
		item2.setBarcode("B0002");
		item2.setTitle("B0002商品");
		item2.setDay(20);
		item2.setOriginPrice(new BigDecimal("20"));
		itemList.add(item2);
		
		Item item3 = new Item();
		item3.setBarcode("C0003");
		item3.setTitle("C0003商品");
		item3.setDay(30);
		item3.setOriginPrice(new BigDecimal("30"));
		itemList.add(item3);
		
		
	}
	
	/**
	 * 原先的条件判断，处理商品的展示信息
	 */
	@Test
	public void barcodeTest() {
		
		itemList.forEach(
				n -> {
					showPrice(n);
					log.info("show {} showPrice is {}", n.getBarcode(), n.getShowPrice());
				}
		);
	}
	
	/**
	 * 编写复杂的条件规则
	 * <p>
	 * 如果是 条码以A 开头， 为100
	 * 条码以B 开头， 为200
	 * 条码不是A,B, 那么按照 生产日期进行打折
	 *
	 * @param item 商品对象
	 *             <p>
	 *             条件各种组装，非常麻烦的。
	 */
	private void showPrice(Item item) {
		String barcode = item.getBarcode();
		if (barcode.startsWith("A")) {
			item.setShowPrice(new BigDecimal("100"));
		} else if (barcode.startsWith("B")) {
			item.setShowPrice(new BigDecimal("200"));
		} else {
			//是 C 的话
			int day = item.getDay();
			if (day > 30) {
				item.setShowPrice(NumberUtil.mul(item.getOriginPrice(), new BigDecimal("0.5")));
			} else if (day > 20) {
				item.setShowPrice(NumberUtil.mul(item.getOriginPrice(), new BigDecimal("0.8")));
			} else {
				item.setShowPrice(NumberUtil.mul(item.getOriginPrice(), new BigDecimal("1.0")));
			}
		}
	}
	
	
}
