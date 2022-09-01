package top.yueshushu.pay.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.pay.business.PayBusiness;
import top.yueshushu.pay.enumtype.PayType;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.pojo.RefundInfo;
import top.yueshushu.pay.service.OrderInfoService;
import top.yueshushu.pay.service.RefundInfoService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-01
 */
@Component
@Slf4j
public class WxTask {
	@Resource
	private OrderInfoService orderInfoService;
	@Resource
	private PayBusiness payBusiness;
	@Resource
	private RefundInfoService refundInfoService;
	
	/**
	 * 从第0秒开始每隔30秒执行1次，查询创建超过5分钟，并且未支付的订单
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void orderConfirm() throws Exception {
		
		List<OrderInfo> orderInfoList = orderInfoService.getNoPayOrderByDuration(1, PayType.WXPAY.getCode());
		
		if (CollectionUtils.isEmpty(orderInfoList)) {
			return;
		}
		
		for (OrderInfo orderInfo : orderInfoList) {
			log.warn("超时订单 ===> {}", orderInfo.getOrderNo());
			
			//核实订单状态：调用微信支付查单接口
			payBusiness.handlerNotPay(orderInfo);
		}
	}
	
	/**
	 * 从第0秒开始每隔30秒执行1次，查询创建超过5分钟，并且未成功的退款单
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void refundConfirm() throws Exception {
		log.info("refundConfirm 被执行......");
		
		//找出申请退款超过5分钟并且未成功的退款单
		List<RefundInfo> refundInfoList = refundInfoService.getNoRefundOrderByDuration(1);
		
		for (RefundInfo refundInfo : refundInfoList) {
			log.warn("超时未退款的退款单号 ===> {}", refundInfo.getRefundNo());
			
			//核实订单状态：调用微信支付查询退款接口
			payBusiness.handlerRefundProcessing(refundInfo);
		}
	}
}
