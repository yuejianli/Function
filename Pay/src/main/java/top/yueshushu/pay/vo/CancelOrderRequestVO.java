package top.yueshushu.pay.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 取消时使用
 *
 * @author yuejianli
 * @date 2022-09-01
 */
@Data
public class CancelOrderRequestVO implements Serializable {
	private String orderNo;
}
