package top.yueshushu.enumtype;

import lombok.Getter;

/**
 * 重试的状态
 *
 * @author xin.liu
 * @date 2020-05-28
 */
@SuppressWarnings("unused")
@Getter
public enum RevokeTypeEnum {
	
	
	AUTO(0, "自动"),
	MANUAL(1, "手动"),
	;
	
	private Integer value;
	private String msg;
	
	RevokeTypeEnum(Integer value, String msg) {
		this.value = value;
		this.msg = msg;
	}
	
	public boolean equalsByValue(Integer value) {
		return this.value.equals(value);
	}
	
	public static RevokeTypeEnum getByValue(Integer value) {
		
		if (null == value) {
			return null;
		}
		
		for (RevokeTypeEnum typeEnum : RevokeTypeEnum.values()) {
			if (typeEnum.value.equals(value)) {
				return typeEnum;
			}
		}
		return null;
	}
}
