package top.yueshushu.pay.enumtype;

import lombok.Getter;

/**
 * 数据状态
 */
@SuppressWarnings("unused")
@Getter
public enum TradeTypeEnum {
    
    NOT_FUND(0, "NOT_FUND"),
    NATIVE(1, "NATIVE"),
    WEB(2, "WEB"),
    ;
    
    private Integer value;
    private String msg;
    
    TradeTypeEnum(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }
    
    public boolean equalsByValue(Integer value) {
        return this.value.equals(value);
    }
    
    public static TradeTypeEnum getByValue(String value) {
        
        if (null == value) {
            return NOT_FUND;
        }
        
        for (TradeTypeEnum typeEnum : TradeTypeEnum.values()) {
            if (typeEnum.msg.equalsIgnoreCase(value)) {
                return typeEnum;
            }
        }
        return NOT_FUND;
    }
}
