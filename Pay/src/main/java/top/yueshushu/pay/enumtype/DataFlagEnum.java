package top.yueshushu.pay.enumtype;

import lombok.Getter;

/**
 * 数据状态
 */
@SuppressWarnings("unused")
@Getter
public enum DataFlagEnum {
	
	
	LOGICALLY_DELETE(0, "逻辑删除"),
	VALID(1, "生效状态"),
	;
	
	private Integer value;
	private String msg;
	
	DataFlagEnum(Integer value, String msg) {
		this.value = value;
		this.msg = msg;
	}
	
	public boolean equalsByValue(Integer value) {
		return this.value.equals(value);
	}
	
	public static DataFlagEnum getByValue(Integer value) {
		
		if (null == value) {
			return null;
		}
		
		for (DataFlagEnum typeEnum : DataFlagEnum.values()) {
			if (typeEnum.value.equals(value)) {
				return typeEnum;
			}
		}
		return null;
	}
}
