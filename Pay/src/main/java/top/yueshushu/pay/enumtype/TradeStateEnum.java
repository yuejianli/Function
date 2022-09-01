package top.yueshushu.pay.enumtype;

import lombok.Getter;

/**
 * 数据状态
 */
@SuppressWarnings("unused")
@Getter
public enum TradeStateEnum {
	
	NOT_FUND(0, "NOT_FUND"),
	
	/**
	 * 支付成功
	 */
	SUCCESS(1, "SUCCESS"),
	
	/**
	 * 未支付
	 */
	NOTPAY(2, "NOTPAY"),
	
	/**
	 * 已关闭
	 */
	CLOSED(3, "CLOSED"),
	
	/**
	 * 转入退款
	 */
	REFUND(4, "REFUND");;
	
	private Integer value;
	private String msg;
	
	TradeStateEnum(Integer value, String msg) {
		this.value = value;
		this.msg = msg;
	}
	
	public boolean equalsByValue(Integer value) {
		return this.value.equals(value);
	}
	
	public static TradeStateEnum getByValue(String value) {
		
		if (null == value) {
			return NOT_FUND;
		}
		
		for (TradeStateEnum typeEnum : TradeStateEnum.values()) {
			if (typeEnum.msg.equalsIgnoreCase(value)) {
				return typeEnum;
			}
		}
		return NOT_FUND;
	}
}
