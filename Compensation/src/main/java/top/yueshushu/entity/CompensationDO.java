package top.yueshushu.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 服务补偿表
 * </p>
 *
 * @author yuejianli 自定义的
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("compensation")
public class CompensationDO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * id主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	
	/**
	 * 标识使用的key值，是 类名+方法名+参数 构成的md5加密值
	 */
	@TableField("bus_key")
	private String busKey;
	
	/**
	 * 完全限定类名
	 */
	@TableField("class_name")
	private String className;
	
	/**
	 * 使用的 bean name,不填写默认为 类名首字母小写
	 */
	@TableField("bean_name")
	private String beanName;
	
	/**
	 * 方法名
	 */
	@TableField("method_name")
	private String methodName;
	
	/**
	 * 参数值
	 */
	@TableField("param_value")
	private String paramValue;
	
	/**
	 * 参数类型
	 */
	@TableField("param_type")
	private String paramType;
	
	/**
	 * 真实参数类型
	 */
	@TableField("param_real_type")
	private String paramRealType;
	
	/**
	 * 执行类型 0为自动 1为手动
	 */
	@TableField("revoke_type")
	private Integer revokeType;
	
	/**
	 * 执行状态  0为重试期间 1为成功 2为失败
	 */
	@TableField("revoke_status")
	private Integer revokeStatus;
	
	/**
	 * 最大重试次数,默认为3次
	 */
	@TableField("retry_count")
	private Integer retryCount;
	/**
	 * 已经重试次数
	 */
	@TableField("has_retry_count")
	private Integer hasRetryCount;
	/**
	 * 最大重试次数,默认为3次
	 */
	@TableField("exception_message")
	private String exceptionMessage;
	
	/**
	 * 创建时间
	 */
	@TableField("create_date")
	private Date createDate;
	
	/**
	 * 更新时间
	 */
	@TableField("update_date")
	private Date updateDate;
	
	/**
	 * 1为正常 0为删除记录
	 */
	@TableField("flag")
	@TableLogic(value = "1", delval = "0")
	private Integer flag;
	
	
}
