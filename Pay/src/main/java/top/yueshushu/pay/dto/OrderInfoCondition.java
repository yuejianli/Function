package top.yueshushu.pay.dto;

import java.io.Serializable;

import lombok.Data;
import top.yueshushu.pay.enumtype.OrderStatus;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@Data
public class OrderInfoCondition implements Serializable {
	private Integer customerId;
	private Integer productId;
	private Integer paymentType;
	private OrderStatus orderStatus;
	private Boolean emptyInsert;
	private String ipAddr;
}
