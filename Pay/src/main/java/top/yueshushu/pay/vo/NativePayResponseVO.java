package top.yueshushu.pay.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 下单的 响应 VO
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@Data
public class NativePayResponseVO implements Serializable {
	private Integer productId;
	private String codeUrl;
	private String orderNo;
}
