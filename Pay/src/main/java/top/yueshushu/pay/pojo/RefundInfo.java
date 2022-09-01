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
 * 退款单信息
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("refund_info")
public class RefundInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * id编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 商户订单编号
     */
    @TableField("order_no")
    private String orderNo;
    
    /**
     * 商户退款单编号，雪花算法生成
     */
    @TableField("refund_no")
    private String refundNo;
    
    /**
     * 支付系统退款单号
     */
    @TableField("refund_id")
    private String refundId;
    
    /**
     * 原订单金额
     */
    @TableField("total_fee")
    private BigDecimal totalFee;
    
    /**
     * 退款金额
     */
    @TableField("refund")
    private BigDecimal refund;
    
    /**
     * 退款原因
     */
    @TableField("reason")
    private String reason;
    
    /**
     * 退款状态
     */
    @TableField("refund_status")
    private Integer refundStatus;
    
    /**
     * 申请退款返回参数
     */
    @TableField("content_return")
    private String contentReturn;
    
    /**
     * 退款结果通知参数
     */
    @TableField("content_notify")
    private String contentNotify;
    
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
