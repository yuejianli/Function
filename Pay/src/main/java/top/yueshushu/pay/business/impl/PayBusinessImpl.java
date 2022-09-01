package top.yueshushu.pay.business.impl;

import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.pay.business.PayBusiness;
import top.yueshushu.pay.config.AlipayPropertiesConfig;
import top.yueshushu.pay.config.WxPayConfig;
import top.yueshushu.pay.dto.OrderInfoCondition;
import top.yueshushu.pay.enumtype.OrderStatus;
import top.yueshushu.pay.enumtype.PayType;
import top.yueshushu.pay.enumtype.wxpay.AliPayTradeState;
import top.yueshushu.pay.enumtype.wxpay.WxRefundStatus;
import top.yueshushu.pay.enumtype.wxpay.WxTradeState;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.pojo.RefundInfo;
import top.yueshushu.pay.response.OutputResult;
import top.yueshushu.pay.service.IPayService;
import top.yueshushu.pay.service.OrderInfoService;
import top.yueshushu.pay.service.PaymentInfoService;
import top.yueshushu.pay.service.RefundInfoService;
import top.yueshushu.pay.service.impl.AliPayServiceImpl;
import top.yueshushu.pay.service.impl.WxPay2ServiceImpl;
import top.yueshushu.pay.service.impl.WxPay3ServiceImpl;
import top.yueshushu.pay.util.PayUtil;
import top.yueshushu.pay.vo.*;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@Service
@Slf4j
public class PayBusinessImpl implements PayBusiness {
	@Resource
	private OrderInfoService orderInfoService;
	@Resource
	private WxPay3ServiceImpl wxPay3ServiceImpl;
	@Resource
	private WxPay2ServiceImpl wxPay2ServiceImpl;
	@Resource
	private AliPayServiceImpl aliPayServiceImpl;
	@Resource
	private WxPayConfig wxPayConfig;
	@Resource
	private PaymentInfoService paymentInfoService;
	@Resource
	private RefundInfoService refundInfoService;
	@Resource
	private AlipayPropertiesConfig alipayPropertiesConfig;
	
	private ReentrantLock lock = new ReentrantLock();
	
	@Override
	public OutputResult<NativePayResponseVO> nativePay(NativePayRequestVO nativePayVO) {
		
		//1. 根据 商品编号 和类型， 查询相应的信息.
		
		OrderInfo orderInfo = orderInfoService.getOrderInfoByCondition(createNativeOrderInfo(nativePayVO));
		
		NativePayResponseVO result = new NativePayResponseVO();
		result.setProductId(orderInfo.getProductId());
		result.setOrderNo(orderInfo.getOrderNo());
		
		if (!StringUtils.isEmpty(orderInfo.getCodeUrl())) {
			result.setCodeUrl(orderInfo.getCodeUrl());
			return OutputResult.buildSucc(result);
		}
		
		// 进行调用处理
		
		IPayService payService = buildPayService(nativePayVO.getPayType(), nativePayVO.getApiType());
		
		try {
			String codeUrl = payService.nativePay(orderInfo);
			
			// 对数据进行处理
			result.setCodeUrl(codeUrl);
			
			orderInfoService.updateCodeUrl(orderInfo.getOrderNo(), codeUrl);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return OutputResult.buildSucc(result);
	}
	
	@Override
	public OutputResult wxNativeNotify(HttpServletRequest httpServletRequest) {
		
		// 对数据进行处理验证.
		
		Map<String, Object> notifyMap = buildPayService(PayType.WXPAY.getCode(), 3).nativeNotify(httpServletRequest);
		
		if (CollectionUtils.isEmpty(notifyMap)) {
			return OutputResult.buildFail();
		}
		// 对数据进行验证。
		String plainText = null;
		try {
			plainText = PayUtil.decryptFromResource(notifyMap, wxPayConfig.getApiV3Key());
		} catch (GeneralSecurityException e) {
			log.info(">>> 解析错误", e);
		}
		
		// 对信息进行转换，转换成 map
		Map<String, Object> paramMap = JSONObject.parseObject(plainText, HashMap.class);
		
		//获取编号
		String orderNo = paramMap.get("out_trade_no").toString();
		
		if (lock.tryLock()) {
			
			try {
				//处理重复的通知
				//接口调用的幂等性：无论接口被调用多少次，产生的结果是一致的。
				// 进行判断， 该编号是否是运行中.
				OrderInfo orderInfo = orderInfoService.getByOrder(orderNo);
				if (orderInfo == null || !OrderStatus.NOTPAY.getCode().equals(orderInfo.getOrderStatus())) {
					return OutputResult.buildSucc();
				}
				
				//模拟通知并发
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//更新订单状态
				orderInfoService.updateStatus(orderNo, OrderStatus.SUCCESS);
				
				//记录支付日志
				paymentInfoService.insertPaymentInfo(paramMap);
			} finally {
				//要主动释放锁
				lock.unlock();
			}
		}
		return OutputResult.buildSucc();
	}
	
	@Override
	public OutputResult aliNativeNotify(Map<String, String> params) {
		boolean rsaCheckV1 = buildPayService(PayType.ALIPAY.getCode(), null).rsaCheckV1(params);
		
		if (!rsaCheckV1) {
			//验签失败则记录异常日志，并在response中返回failure.
			log.error("支付成功异步通知验签失败！");
			return OutputResult.buildFail();
		}
		
		// 验签成功后
		log.info("支付成功异步通知验签成功！");
		
		//按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
		//1 商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号
		String outTradeNo = params.get("out_trade_no");
		OrderInfo orderInfo = orderInfoService.getByOrder(outTradeNo);
		if (orderInfo == null) {
			log.error("订单不存在");
			return OutputResult.buildFail();
		}
		
		//2 判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额）
		String totalAmount = params.get("total_amount");
		int totalAmountInt = new BigDecimal(totalAmount).multiply(new BigDecimal("100")).intValue();
		int totalFeeInt = PayUtil.getFee(orderInfo.getTotalFee());
		if (totalAmountInt != totalFeeInt) {
			log.error("金额校验失败");
			return OutputResult.buildFail();
		}
		
		//3 校验通知中的 seller_id（或者 seller_email) 是否为 out_trade_no 这笔单据的对应的操作方
		String sellerId = params.get("seller_id");
		String sellerIdProperty = alipayPropertiesConfig.getSellerId();
		if (!sellerId.equals(sellerIdProperty)) {
			log.error("商家pid校验失败");
			return OutputResult.buildFail();
		}
		
		//4 验证 app_id 是否为该商户本身
		String appId = params.get("app_id");
		String appIdProperty = alipayPropertiesConfig.getAppId();
		if (!appId.equals(appIdProperty)) {
			log.error("appid校验失败");
			return OutputResult.buildFail();
		}
		
		//在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS时，
		// 支付宝才会认定为买家付款成功。
		String tradeStatus = params.get("trade_status");
		if (!"TRADE_SUCCESS".equals(tradeStatus)) {
			log.error("支付未成功");
			return OutputResult.buildFail();
		}
		if (lock.tryLock()) {
			
			try {
				//处理重复的通知
				//接口调用的幂等性：无论接口被调用多少次，产生的结果是一致的。
				// 进行判断， 该编号是否是运行中.
				//模拟通知并发
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//更新订单状态
				orderInfoService.updateStatus(orderInfo.getOrderNo(), OrderStatus.SUCCESS);
				
				//记录支付日志
				paymentInfoService.insertAliPaymentInfo(params);
			} finally {
				//要主动释放锁
				lock.unlock();
			}
		}
		return OutputResult.buildSucc();
	}
	
	@Override
	public OutputResult wx2NativeNotify(HttpServletRequest httpServletRequest) {
		
		// 对数据进行处理验证.
		
		Map<String, Object> notifyMap = buildPayService(PayType.WXPAY.getCode(), 2).nativeNotify(httpServletRequest);
		
		if (CollectionUtils.isEmpty(notifyMap)) {
			return OutputResult.buildFail();
		}
		
		// 获取 订单号
		String orderNo = notifyMap.get("out_trade_no").toString();
		OrderInfo orderInfo = orderInfoService.getByOrder(orderNo);
		//并校验返回的订单金额是否与商户侧的订单金额一致
		if (orderInfo != null && orderInfo.getTotalFee().equals(PayUtil.formatFee(Integer.parseInt(notifyMap.get("total_fee").toString())))) {
			log.error("金额校验失败");
			return OutputResult.buildFail();
		}
		
		if (lock.tryLock()) {
			
			try {
				//处理重复的通知
				//接口调用的幂等性：无论接口被调用多少次，产生的结果是一致的。
				if (orderInfo == null || !OrderStatus.NOTPAY.getCode().equals(orderInfo.getOrderStatus())) {
					return OutputResult.buildSucc();
				}
				
				//模拟通知并发
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//更新订单状态
				orderInfoService.updateStatus(orderNo, OrderStatus.SUCCESS);
				
				//记录支付日志
				paymentInfoService.insertPaymentInfo(notifyMap);
			} finally {
				//要主动释放锁
				lock.unlock();
			}
		}
		return OutputResult.buildSucc();
	}
	
	@Override
	public OutputResult cancelOrder(CancelOrderRequestVO cancelOrderRequestVO) {
		//1. 根据订单号获取相应的信息.
		String orderNo = cancelOrderRequestVO.getOrderNo();
		OrderInfo orderInfo = orderInfoService.getByOrder(orderNo);
		
		IPayService payService = buildPayService(orderInfo.getPaymentType(), 3);
		
		try {
			boolean cancelFlag = payService.cancelOrder(orderNo);
			// 更新订单状态
			if (cancelFlag) {
				orderInfoService.updateStatus(orderNo, OrderStatus.CANCEL);
				return OutputResult.buildSucc();
			}
			return OutputResult.buildFail();
		} catch (Exception e) {
			log.error(">>>> 取消订单失败", e);
			return OutputResult.buildFail();
		}
	}
	
	@Override
	public OutputResult queryOrder(String orderNo) {
		//1. 根据订单号获取相应的信息.
		OrderInfo orderInfo = orderInfoService.getByOrder(orderNo);
		
		IPayService payService = buildPayService(orderInfo.getPaymentType(), 3);
		
		try {
			String bodyAsString = payService.queryOrder(orderNo);
			// 更新订单状态
			if (StringUtils.hasText(bodyAsString)) {
				return OutputResult.buildSucc(bodyAsString);
			}
			return OutputResult.buildFail();
		} catch (Exception e) {
			log.error(">>>> 查询订单失败", e);
			return OutputResult.buildFail();
		}
	}
	
	@Override
	public OutputResult refunds(RefundsInfoRequestVO refundsInfoRequestVO) {
		//1. 创建退款单
		OrderInfo orderInfo = orderInfoService.getByOrder(refundsInfoRequestVO.getOrderNo());
		RefundInfo refundInfo = refundInfoService.createRefunds(orderInfo, refundsInfoRequestVO.getReason());
		
		// 调用信息，进行申请退款.
		
		IPayService payService = buildPayService(orderInfo.getPaymentType(), 3);
		
		String refundsResponseStr = payService.refunds(refundInfo);
		
		if (StringUtils.isEmpty(refundsResponseStr)) {
			// 更新状态
			orderInfoService.updateStatus(orderInfo.getOrderNo(), OrderStatus.REFUND_ABNORMAL);
			// 更新退款单信息
			
			refundInfo.setContentReturn("退款失败");
			refundInfo.setUpdateTime(DateUtil.date());
			refundInfoService.updateById(refundInfo);
			
			return OutputResult.buildSucc();
		} else {
			// 更新状态
			orderInfoService.updateStatus(orderInfo.getOrderNo(), OrderStatus.REFUND_PROCESSING);
			
			// 更新退款单信息
			
			refundInfo.setContentReturn(refundsResponseStr);
			refundInfo.setUpdateTime(DateUtil.date());
			refundInfoService.updateById(refundInfo);
			
			return OutputResult.buildSucc();
		}
	}
	
	@Override
	public OutputResult queryRefunds(String refundNo) {
		//1. 根据退款单编号查询出 订单编号
		
		RefundInfo refundInfo = refundInfoService.getByRefundNo(refundNo);
		//查询出订单号
		OrderInfo orderInfo = orderInfoService.getByOrder(refundInfo.getOrderNo());
		
		//构建信息
		
		IPayService payService = buildPayService(orderInfo.getPaymentType(), 3);
		
		String bodyAsString = payService.queryRefunds(orderInfo.getOrderNo());
		
		return OutputResult.buildSucc(bodyAsString);
		
	}
	
	@Override
	public OutputResult wxRefundsNotify(HttpServletRequest httpServletRequest) {
		
		// 对数据进行处理验证.
		
		Map<String, Object> notifyMap = buildPayService(PayType.WXPAY.getCode(), 3).refundsNotify(httpServletRequest);
		
		if (CollectionUtils.isEmpty(notifyMap)) {
			return OutputResult.buildFail();
		}
		// 对数据进行验证。
		String plainText = null;
		try {
			plainText = PayUtil.decryptFromResource(notifyMap, wxPayConfig.getApiV3Key());
		} catch (GeneralSecurityException e) {
			log.info(">>> 解析错误", e);
		}
		
		// 对信息进行转换，转换成 map
		Map<String, Object> paramMap = JSONObject.parseObject(plainText, HashMap.class);
		
		//获取编号
		String orderNo = paramMap.get("out_trade_no").toString();
		
		if (lock.tryLock()) {
			
			try {
				// 进行判断， 该编号是否是运行中.
				OrderInfo orderInfo = orderInfoService.getByOrder(orderNo);
				if (orderInfo == null || !OrderStatus.REFUND_PROCESSING.getCode().equals(orderInfo.getOrderStatus())) {
					return OutputResult.buildSucc();
				}
				
				//更新订单状态
				orderInfoService.updateStatus(orderNo, OrderStatus.REFUND_SUCCESS);
				
				//更新退款信息
				refundInfoService.updateRefunds(paramMap);
			} finally {
				//要主动释放锁
				lock.unlock();
			}
		}
		return OutputResult.buildSucc();
	}
	
	@Override
	public String wxQueryTradeBill(BillRequestVO billRequestVO) {
		IPayService payService = buildPayService(PayType.WXPAY.getCode(), 3);
		return payService.queryTradeBill(billRequestVO);
	}
	
	@Override
	public String wxDownloadTradeBill(BillRequestVO billRequestVO) {
		IPayService payService = buildPayService(PayType.WXPAY.getCode(), 3);
		String result = payService.downloadTradeBill(billRequestVO);
		
		return result;
	}
	
	@Override
	public void handlerNotPay(OrderInfo orderInfo) {
		log.warn("根据订单号核实订单状态 ===> {}", orderInfo.getOrderNo());
		
		IPayService payService = buildPayService(orderInfo.getPaymentType(), 3);
		
		//调用微信支付查单接口
		String result = payService.queryOrder(orderInfo.getOrderNo());
		
		Map<String, Object> resultMap = JSONObject.parseObject(result, HashMap.class);
		
		//获取微信支付端的订单状态
		String tradeState = resultMap.get("trade_state").toString();
		
		//判断订单状态
		if (WxTradeState.SUCCESS.getType().equals(tradeState)) {
			
			log.warn("核实订单已支付 ===> {}", orderInfo.getOrderNo());
			
			//如果确认订单已支付则更新本地订单状态
			orderInfoService.updateStatus(orderInfo.getOrderNo(), OrderStatus.SUCCESS);
			//记录支付日志
			paymentInfoService.insertPaymentInfo(resultMap);
		}
		
		if (WxTradeState.NOTPAY.getType().equals(tradeState)) {
			log.warn("核实订单未支付 ===> {}", orderInfo.getOrderNo());
			
			//如果订单未支付，则调用关单接口
			payService.cancelOrder(orderInfo.getOrderNo());
			
			orderInfoService.updateStatus(orderInfo.getOrderNo(), OrderStatus.CLOSED);
		}
	}
	
	@Override
	public void handlerRefundProcessing(RefundInfo refundInfo) {
		
		String orderNo = refundInfo.getOrderNo();
		log.warn("根据退款单号核实退款单状态 ===> {}", orderNo);
		
		OrderInfo orderInfo = orderInfoService.getByOrder(orderNo);
		
		IPayService payService = buildPayService(orderInfo.getPaymentType(), 3);
		
		//调用查询退款单接口
		String result = payService.queryRefunds(refundInfo.getRefundNo());
		
		//组装json请求体字符串
		Map<String, Object> resultMap = JSONObject.parseObject(result, HashMap.class);
		
		//获取微信支付端退款状态
		String status = resultMap.get("status").toString();
		
		
		if (WxRefundStatus.SUCCESS.getName().equals(status)) {
			
			log.warn("核实订单已退款成功 ===> {}", refundInfo.getRefundNo());
			
			//如果确认退款成功，则更新订单状态
			orderInfoService.updateStatus(orderNo, OrderStatus.REFUND_SUCCESS);
			
			//更新退款单
			refundInfoService.updateRefunds(resultMap);
		}
		
		if (WxRefundStatus.ABNORMAL.getName().equals(status)) {
			
			log.warn("核实订单退款异常  ===> {}", refundInfo.getRefundNo());
			
			//如果确认退款成功，则更新订单状态
			orderInfoService.updateStatus(orderNo, OrderStatus.REFUND_ABNORMAL);
			
			//更新退款单
			refundInfoService.updateRefunds(resultMap);
		}
	}
	
	@Override
	public String aliQueryTradeBill(BillRequestVO billRequestVO) {
		IPayService iPayService = buildPayService(PayType.ALIPAY.getCode(), null);
		
		return iPayService.queryTradeBill(billRequestVO);
	}
	
	@Override
	public void handlerAliNotPay(OrderInfo orderInfo) {
		log.warn("根据订单号核实订单状态 ===> {}", orderInfo.getOrderNo());
		
		IPayService payService = buildPayService(orderInfo.getPaymentType(), 3);
		
		//调用微信支付查单接口
		String result = payService.queryOrder(orderInfo.getOrderNo());
		
		Map<String, LinkedHashMap> resultMap = JSONObject.parseObject(result, HashMap.class);
		
		//获取微信支付端的订单状态
		LinkedHashMap linkedHashMap = resultMap.get("alipay_trade_query_response");
		
		String tradeState = (String) linkedHashMap.get("trade_status");
		
		
		//判断订单状态
		if (AliPayTradeState.SUCCESS.getType().equals(tradeState)) {
			
			log.warn("核实订单已支付 ===> {}", orderInfo.getOrderNo());
			
			//如果确认订单已支付则更新本地订单状态
			orderInfoService.updateStatus(orderInfo.getOrderNo(), OrderStatus.SUCCESS);
			//记录支付日志
			paymentInfoService.insertAliPaymentInfo(linkedHashMap);
		}
		
		if (AliPayTradeState.NOTPAY.getType().equals(tradeState)) {
			log.warn("核实订单未支付 ===> {}", orderInfo.getOrderNo());
			
			//如果订单未支付，则调用关单接口
			payService.cancelOrder(orderInfo.getOrderNo());
			
			orderInfoService.updateStatus(orderInfo.getOrderNo(), OrderStatus.CLOSED);
		}
	}
	
	private IPayService buildPayService(Integer payType, Integer apiType) {
		if (PayType.WXPAY.getCode().equals(payType)) {
			if (apiType == 2) {
				return wxPay2ServiceImpl;
			} else {
				return wxPay3ServiceImpl;
			}
		} else if (PayType.ALIPAY.getCode().equals(payType)) {
			return aliPayServiceImpl;
		}
		return null;
	}
	
	private OrderInfoCondition createNativeOrderInfo(NativePayRequestVO nativePayVO) {
		OrderInfoCondition orderInfoCondition = new OrderInfoCondition();
		orderInfoCondition.setCustomerId(1);
		orderInfoCondition.setProductId(nativePayVO.getProductId());
		orderInfoCondition.setPaymentType(nativePayVO.getPayType());
		orderInfoCondition.setOrderStatus(OrderStatus.NOTPAY);
		orderInfoCondition.setEmptyInsert(true);
		orderInfoCondition.setIpAddr(nativePayVO.getIpAddr());
		return orderInfoCondition;
	}
}
