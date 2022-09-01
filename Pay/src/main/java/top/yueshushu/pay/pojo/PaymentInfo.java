package top.yueshushu.pay.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 支付信息表
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("payment_info")
public class PaymentInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * id编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 订单编号, order_info 中的编号
     */
    @TableField("order_no")
    private String orderNo;
    
    /**
     * 支付系统对应的交易编号
     */
    @TableField("transaction_id")
    private String transactionId;
    
    /**
     * 支付类型 1为微信 2为支付宝
     */
    @TableField("payment_type")
    private Integer paymentType;
    
    /**
     * 交易类型
     */
    @TableField("trade_type")
    private Integer tradeType;
    
    /**
     * 交易状态
     */
    @TableField("trade_state")
    private Integer tradeState;
    
    /**
     * 支付金额
     */
    @TableField("payer_total")
    private BigDecimal payerTotal;
    
    /**
     * 通知参数
     */
    @TableField("content")
    private String content;
    
    /**
     * 创建人
     */
    @TableField("create_id")
    private Integer createId;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    
    /**
     * 更新人
     */
    @TableField("update_id")
    private Integer updateId;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    
    /**
     * 标识 1为正常 0为删除
     */
    @TableField("flag")
    private Integer flag;
    
    
}
