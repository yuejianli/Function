package top.yueshushu.pay.enumtype;

import lombok.Getter;

@Getter
public enum PayType {
    /**
     * 微信
     */
    WXPAY(1, "微信"),
    
    
    /**
     * 支付宝
     */
    ALIPAY(2, "支付宝");
    
    /**
     * 类型
     */
    private Integer code;
    private String name;
    
    PayType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
