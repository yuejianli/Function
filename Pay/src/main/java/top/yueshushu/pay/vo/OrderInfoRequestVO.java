package top.yueshushu.pay.vo;

import java.io.Serializable;

import lombok.Data;
import top.yueshushu.pay.response.PageRo;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-31
 */
@Data
public class OrderInfoRequestVO extends PageRo implements Serializable {
	private Integer customerId;
	private String orderNo;
}
