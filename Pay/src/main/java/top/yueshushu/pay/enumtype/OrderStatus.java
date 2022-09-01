package top.yueshushu.pay.enumtype;

import lombok.Getter;

@Getter
public enum OrderStatus {
    /**
     * 未支付
     */
    NOTPAY(1, "未支付"),
    
    
    /**
     * 支付成功
     */
    SUCCESS(2, "支付成功"),
    
    /**
     * 已关闭
     */
    CLOSED(3, "超时已关闭"),
    
    /**
     * 已取消
     */
    CANCEL(4, "用户已取消"),
    
    /**
     * 退款中
     */
    REFUND_PROCESSING(5, "退款中"),
    
    /**
     * 已退款
     */
    REFUND_SUCCESS(6, "已退款"),
    
    /**
     * 退款异常
     */
    REFUND_ABNORMAL(7, "退款异常");
    
    /**
     * 类型
     */
    private Integer code;
    private String name;
    
    OrderStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public static OrderStatus getByValue(Integer value) {
        
        if (null == value) {
            return null;
        }
        
        for (OrderStatus typeEnum : OrderStatus.values()) {
            if (typeEnum.code.equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
