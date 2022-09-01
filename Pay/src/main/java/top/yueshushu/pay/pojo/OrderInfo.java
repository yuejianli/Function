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
 * 订单表
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("order_info")
public class OrderInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * id编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 订单编号,雪花算法生成
     */
    @TableField("order_no")
    private String orderNo;
    
    /**
     * 订单标题
     */
    @TableField("title")
    private String title;
    
    /**
     * 商品id
     */
    @TableField("product_id")
    private Integer productId;
    
    /**
     * 用户id,默认为1
     */
    @TableField("customer_id")
    private Integer customerId;
    
    /**
     * 支付类型 1为微信 2为支付宝 3为银行卡
     */
    @TableField("payment_type")
    private Integer paymentType;
    
    /**
     * 订单金额
     */
    @TableField("total_fee")
    private BigDecimal totalFee;
    
    /**
     * 订单的状态
     */
    @TableField("order_status")
    private Integer orderStatus;
    
    /**
     * 订单二维码链接
     */
    @TableField("code_url")
    private String codeUrl;
    
    /**
     * 请求的地址
     */
    @TableField("ip_addr")
    private String ipAddr;
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
