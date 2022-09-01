package top.yueshushu.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.pay.config.AlipayPropertiesConfig;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.pojo.RefundInfo;
import top.yueshushu.pay.service.IPayService;
import top.yueshushu.pay.util.PayUtil;
import top.yueshushu.pay.vo.BillRequestVO;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@Service("aliPayService")
@Slf4j
public class AliPayServiceImpl implements IPayService {
	@Resource
	private AlipayPropertiesConfig alipayPropertiesConfig;
	@Resource
	private AlipayClient alipayClient;
	
	@Override
	public String nativePay(OrderInfo orderInfo) throws Exception {
		//调用支付宝接口
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		//配置需要的公共请求参数
		//支付完成后，支付宝向用户发起异步通知的地址
		request.setNotifyUrl(alipayPropertiesConfig.getNotifyUrl());
		//支付完成后，我们想让页面跳转回谷粒学院的页面，配置returnUrl
		request.setReturnUrl(alipayPropertiesConfig.getReturnUrl());
		
		//组装当前业务方法的请求参数
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no", orderInfo.getOrderNo());
		int total = PayUtil.getFee(orderInfo.getTotalFee());
		bizContent.put("total_amount", total);
		bizContent.put("subject", orderInfo.getTitle());
		bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
		
		request.setBizContent(bizContent.toString());
		
		//执行请求，调用支付宝接口
		AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
		
		if (response.isSuccess()) {
			log.info("调用成功，返回结果 ===> " + response.getBody());
			return response.getBody();
		} else {
			log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
			throw new RuntimeException("创建支付交易失败");
		}
	}
	
	@Override
	public Map<String, Object> nativeNotify(HttpServletRequest httpServletRequest) {
		return null;
	}
	
	@Override
	public boolean cancelOrder(String orderNo) {
		try {
			AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
			JSONObject bizContent = new JSONObject();
			bizContent.put("out_trade_no", orderNo);
			request.setBizContent(bizContent.toString());
			AlipayTradeCloseResponse response = alipayClient.execute(request);
			
			if (response.isSuccess()) {
				log.info("调用成功，返回结果 ===> " + response.getBody());
				return true;
			} else {
				log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
				//throw new RuntimeException("关单接口的调用失败");
				return false;
			}
		} catch (Exception e) {
			log.error(" 取消订单失败 {}", e);
			return false;
		}
	}
	
	@Override
	public String queryOrder(String orderNo) {
		try {
			log.info("查单接口调用 ===> {}", orderNo);
			
			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
			JSONObject bizContent = new JSONObject();
			bizContent.put("out_trade_no", orderNo);
			request.setBizContent(bizContent.toString());
			
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				log.info("调用成功，返回结果 ===> " + response.getBody());
				return response.getBody();
			} else {
				log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
				//throw new RuntimeException("查单接口的调用失败");
				//订单不存在
				return null;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new RuntimeException("查单接口的调用失败");
		}
	}
	
	@Override
	public String refunds(RefundInfo refundInfo) {
		//调用统一收单交易退款接口
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		
		//组装当前业务方法的请求参数
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no", refundInfo.getOrderNo());//订单编号
		bizContent.put("refund_amount", refundInfo.getRefund());//退款金额：不能大于支付金额
		bizContent.put("refund_reason", refundInfo.getReason());//退款原因(可选)
		
		request.setBizContent(bizContent.toString());
		
		try {
			//执行请求，调用支付宝接口
			AlipayTradeRefundResponse response = alipayClient.execute(request);
			return response.isSuccess() ? response.getBody() : null;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String queryRefunds(String orderNo) {
		try {
			log.info("查询退款接口调用 ===> {}", orderNo);
			
			AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
			JSONObject bizContent = new JSONObject();
			bizContent.put("out_trade_no", orderNo);
			bizContent.put("out_request_no", orderNo);
			request.setBizContent(bizContent.toString());
			
			AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				log.info("调用成功，返回结果 ===> " + response.getBody());
				return response.getBody();
			} else {
				log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
				//throw new RuntimeException("查单接口的调用失败");
				return null;//订单不存在
			}
			
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Map<String, Object> refundsNotify(HttpServletRequest httpServletRequest) {
		return null;
	}
	
	@Override
	public String queryTradeBill(BillRequestVO billRequestVO) {
		try {
			
			AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
			JSONObject bizContent = new JSONObject();
			bizContent.put("bill_type", billRequestVO.getType());
			bizContent.put("bill_date", billRequestVO.getBillDate());
			request.setBizContent(bizContent.toString());
			AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
			
			if (response.isSuccess()) {
				log.info("调用成功，返回结果 ===> " + response.getBody());
				
				//获取账单下载地址
				HashMap<String, LinkedHashMap> resultMap = JSONObject.parseObject(response.getBody(), HashMap.class);
				LinkedHashMap billDownloadurlResponse = resultMap.get("alipay_data_dataservice_bill_downloadurl_query_response");
				String billDownloadUrl = (String) billDownloadurlResponse.get("bill_download_url");
				
				return billDownloadUrl;
			} else {
				log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
				throw new RuntimeException("申请账单失败");
			}
			
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new RuntimeException("申请账单失败");
		}
	}
	
	@Override
	public String downloadTradeBill(BillRequestVO billRequestVO) {
		return null;
	}
	
	@Override
	public boolean rsaCheckV1(Map<String, String> params) {
		//异步通知验签
		try {
			return AlipaySignature.rsaCheckV1(
					params,
					alipayPropertiesConfig.getAlipayPublicKey(),
					AlipayConstants.CHARSET_UTF8,
					AlipayConstants.SIGN_TYPE_RSA2);
		} catch (Exception e) {
			return false;
		}
	}
}
