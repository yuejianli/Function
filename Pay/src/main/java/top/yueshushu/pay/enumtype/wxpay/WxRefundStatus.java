package top.yueshushu.pay.enumtype.wxpay;

import lombok.Getter;

@Getter
public enum WxRefundStatus {
    
    /**
     * 退款处理中
     */
    PROCESSING(1, "PROCESSING"),
    /**
     * 退款成功
     */
    SUCCESS(2, "SUCCESS"),
    
    /**
     * 退款关闭
     */
    CLOSED(3, "CLOSED"),
    
    /**
     * 退款异常
     */
    ABNORMAL(4, "ABNORMAL"),
    
    ;
    
    
    /**
     * 类型
     */
    private Integer code;
    private String name;
    
    WxRefundStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public static WxRefundStatus getByValue(String value) {
        
        if (null == value) {
            return null;
        }
        
        for (WxRefundStatus typeEnum : WxRefundStatus.values()) {
            if (typeEnum.name.equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
