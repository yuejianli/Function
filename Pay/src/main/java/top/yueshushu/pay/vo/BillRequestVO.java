package top.yueshushu.pay.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 订单信息
 *
 * @author yuejianli
 * @date 2022-09-01
 */
@Data
public class BillRequestVO implements Serializable {
	
	private String billDate;
	
	private String type;
}
