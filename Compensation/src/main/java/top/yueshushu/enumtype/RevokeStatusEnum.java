package top.yueshushu.enumtype;

import lombok.Getter;

/**
 * 执行的状态
 *
 * @author xin.liu
 * @date 2020-05-28
 */
@SuppressWarnings("unused")
@Getter
public enum RevokeStatusEnum {
	
	
	ING(0, "重试中"),
	SUCCESS(1, "成功"),
	FAIL(2, "失败"),
	;
	
	private Integer value;
	private String msg;
	
	RevokeStatusEnum(Integer value, String msg) {
		this.value = value;
		this.msg = msg;
	}
	
	public boolean equalsByValue(Integer value) {
		return this.value.equals(value);
	}
	
	public static RevokeStatusEnum getByValue(Integer value) {
		
		if (null == value) {
			return null;
		}
		
		for (RevokeStatusEnum typeEnum : RevokeStatusEnum.values()) {
			if (typeEnum.value.equals(value)) {
				return typeEnum;
			}
		}
		return null;
	}
}
