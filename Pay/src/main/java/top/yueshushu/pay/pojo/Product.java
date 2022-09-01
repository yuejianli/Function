package top.yueshushu.pay.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product")
public class Product implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * id编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 商品名称
     */
    @TableField("title")
    private String title;
    
    /**
     * 商品价格
     */
    @TableField("price")
    private BigDecimal price;
    
    /**
     * 创建人,默认为1
     */
    @TableField("create_id")
    private Integer createId;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 修改人,默认为1
     */
    @TableField("update_id")
    private Integer updateId;
    
    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    /**
     * 标识,1为正常,0为删除
     */
    @TableField("flag")
    private Integer flag;
    
    
}
