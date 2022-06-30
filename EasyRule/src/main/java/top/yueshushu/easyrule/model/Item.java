package top.yueshushu.easyrule.model;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 商品信息对象
 *
 * @author yuejianli
 * @date 2022-06-29
 */
@Data
public class Item {
	/**
	 * 商品条码
	 */
	private String barcode;
	/**
	 * 商品名称
	 */
	private String title;
	/**
	 * 生产的天数
	 */
	private Integer day;
	/**
	 * 商品原始价格
	 */
	private BigDecimal originPrice;
	/**
	 * 商品展示价格
	 */
	private BigDecimal showPrice;
}
