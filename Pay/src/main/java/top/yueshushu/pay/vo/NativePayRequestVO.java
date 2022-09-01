package top.yueshushu.pay.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@Data
@ApiModel
public class NativePayRequestVO {
	@ApiModelProperty("商品的id")
	private Integer productId;
	@ApiModelProperty("支付类型 1为微信 2为支付宝")
	private Integer payType = 1;
	@ApiModelProperty("api 类型  2为 apiV2, 3为 apiV3")
	private Integer apiType = 3;
	
	@ApiModelProperty("请求的IP地址, 微信 api v2 时会使用")
	private String ipAddr;
}
