package top.yueshushu.pay.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-01
 */
@Data
public class RefundsInfoRequestVO implements Serializable {
	private String orderNo;
	private String reason;
	private String refundNo;
}
