package top.yueshushu.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.pay.config.WxPayConfig;
import top.yueshushu.pay.enumtype.wxpay.WxApiType;
import top.yueshushu.pay.enumtype.wxpay.WxNotifyType;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.pojo.RefundInfo;
import top.yueshushu.pay.service.IPayService;
import top.yueshushu.pay.util.HttpUtils;
import top.yueshushu.pay.util.PayUtil;
import top.yueshushu.pay.util.WechatPay2ValidatorForRequest;
import top.yueshushu.pay.vo.BillRequestVO;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@Service("wxPay3Service")
@Slf4j
public class WxPay3ServiceImpl implements IPayService {
	@Resource
	private WxPayConfig wxPayConfig;
	@Resource(name = "wxPayClient")
	private CloseableHttpClient wxPayClient;
	
	@Resource(name = "wxPayNoSignClient")
	private CloseableHttpClient wxPayNoSignClient;
	
	@Resource
	private Verifier verifier;
	
	@Override
	public String nativePay(OrderInfo orderInfo) throws Exception {
		//调用统一下单API
		HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType()));
		
		//将参数转换成json字符串
		String jsonParams = buildNativeParam(orderInfo);
		
		
		StringEntity entity = new StringEntity(jsonParams, "utf-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		
		//完成签名并执行请求
		CloseableHttpResponse response = wxPayClient.execute(httpPost);
		
		try {
			//响应体
			String bodyAsString = EntityUtils.toString(response.getEntity());
			//响应状态码
			int statusCode = response.getStatusLine().getStatusCode();
			//处理成功
			if (statusCode == 200) {
				log.info("成功, 返回结果 = " + bodyAsString);
				//处理成功，无返回Body
			} else if (statusCode == 204) {
				log.info("成功");
			} else {
				log.info("Native下单失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
				throw new IOException("request failed");
			}
			
			//响应结果 ,响应形式为:  {"code_url":"weixin://wxpay/bizpayurl?pr=3jz9iRzzz"}
			Map<String, String> resultMap = JSONObject.parseObject(bodyAsString, HashMap.class);
			//二维码
			return resultMap.get("code_url");
			
		} finally {
			response.close();
		}
	}
	
	@Override
	public Map<String, Object> nativeNotify(HttpServletRequest httpServletRequest) {
		try {
			String body = HttpUtils.readData(httpServletRequest);
			// 将数据构造成 map
			
			HashMap<String, Object> bodyMap = JSONObject.parseObject(body, HashMap.class);
			
			String requestId = (String) bodyMap.get("id");
			
			log.info(">>> 支付通知的id {}", requestId);
			
			WechatPay2ValidatorForRequest wechatPay2ValidatorForRequest
					= new WechatPay2ValidatorForRequest(verifier, requestId, body);
			
			if (!wechatPay2ValidatorForRequest.validate(httpServletRequest)) {
				// 验证未通过
				return null;
			}
			return bodyMap;
		} catch (Exception e) {
			log.error("微信支付通知出错", e);
			return null;
		}
	}
	
	@Override
	public boolean cancelOrder(String orderNo) {
		log.info(">>>>>取消订单 {}", orderNo);
		
		String url = String.format(WxApiType.CLOSE_ORDER_BY_NO.getType(), orderNo);
		url = wxPayConfig.getDomain().concat(url);
		
		HttpPost httpPost = new HttpPost(url);
		
		StringEntity entity = new StringEntity(buildCancelParam(), "utf-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		
		try {
			
			CloseableHttpResponse closeableHttpResponse = wxPayClient.execute(httpPost);
			try {
				// 响应状态码
				int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
				//200 是处理成功，有返回 Body . 204 没有返回 Body
				if (statusCode == 200 || statusCode == 204) {
					return true;
				} else {
					log.info(">>>>取消支付失败,状态码是:{}", statusCode);
					return false;
				}
			} finally {
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			log.error(">>>取消支付调用接口失败", e);
			return false;
		}
	}
	
	@Override
	public String queryOrder(String orderNo) {
		log.info(">>> 查询订单接口调用 {}", orderNo);
		
		String url = String.format(WxApiType.ORDER_QUERY_BY_NO.getType(), orderNo);
		url = wxPayConfig.getDomain().concat(url)
				.concat("?mchid=").concat(wxPayConfig.getMchId());
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		try {
			
			CloseableHttpResponse closeableHttpResponse = wxPayClient.execute(httpGet);
			try {
				//响应体
				String bodyAsString = EntityUtils.toString(closeableHttpResponse.getEntity());
				// 获取响应状态码
				int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
				
				if (statusCode == 200 || statusCode == 204) {
					return bodyAsString;
				} else {
					log.info(">>>查询订单接口失败,响应码是:{},响应结果是:{}", statusCode, bodyAsString);
					return null;
				}
				
			} finally {
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			log.error(">>>查询订单信息失败", e);
			return null;
		}
	}
	
	@Override
	public String refunds(RefundInfo refundInfo) {
		String url = wxPayConfig.getDomain().concat(WxApiType.DOMESTIC_REFUNDS.getType());
		
		HttpPost httpPost = new HttpPost(url);
		
		
		//将参数转换成json字符串
		String jsonParams = buildRefundsParam(refundInfo);
		
		
		StringEntity entity = new StringEntity(jsonParams, "utf-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		
		try {
			//完成签名并执行请求
			CloseableHttpResponse response = wxPayClient.execute(httpPost);
			try {
				//响应体
				String bodyAsString = EntityUtils.toString(response.getEntity());
				//响应状态码
				int statusCode = response.getStatusLine().getStatusCode();
				//处理成功
				if (statusCode == 200) {
					log.info("成功, 返回结果 = " + bodyAsString);
					//处理成功，无返回Body
				} else if (statusCode == 204) {
					log.info("成功");
				} else {
					log.info("Native下单失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
					throw new IOException("request failed");
				}
				return bodyAsString;
			} finally {
				response.close();
			}
		} catch (Exception e) {
			log.error("申请退款失败", e);
			return null;
		}
	}
	
	@Override
	public String queryRefunds(String refundNo) {
		
		log.info(">>> 查询退款单接口调用 {}", refundNo);
		
		String url = String.format(WxApiType.DOMESTIC_REFUNDS_QUERY.getType(), refundNo);
		url = wxPayConfig.getDomain().concat(url);
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		try {
			
			CloseableHttpResponse closeableHttpResponse = wxPayClient.execute(httpGet);
			try {
				//响应体
				String bodyAsString = EntityUtils.toString(closeableHttpResponse.getEntity());
				// 获取响应状态码
				int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
				
				if (statusCode == 200 || statusCode == 204) {
					return bodyAsString;
				} else {
					log.info(">>>查询订单接口失败,响应码是:{},响应结果是:{}", statusCode, bodyAsString);
					return null;
				}
				
			} finally {
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			log.error(">>>查询订单信息失败", e);
			return null;
		}
		
	}
	
	@Override
	public Map<String, Object> refundsNotify(HttpServletRequest httpServletRequest) {
		try {
			String body = HttpUtils.readData(httpServletRequest);
			// 将数据构造成 map
			
			HashMap<String, Object> bodyMap = JSONObject.parseObject(body, HashMap.class);
			
			String requestId = (String) bodyMap.get("id");
			
			log.info(">>> 支付通知的id {}", requestId);
			
			WechatPay2ValidatorForRequest wechatPay2ValidatorForRequest
					= new WechatPay2ValidatorForRequest(verifier, requestId, body);
			
			if (!wechatPay2ValidatorForRequest.validate(httpServletRequest)) {
				// 验证未通过
				return null;
			}
			return bodyMap;
		} catch (Exception e) {
			log.error("微信支付通知出错", e);
			return null;
		}
	}
	
	@Override
	public String queryTradeBill(BillRequestVO billRequestVO) {
		
		log.warn("申请账单接口调用 {}", billRequestVO.getBillDate());
		
		String url = "";
		if ("tradebill".equals(billRequestVO.getType())) {
			url = WxApiType.TRADE_BILLS.getType();
		} else if ("fundflowbill".equals(billRequestVO.getType())) {
			url = WxApiType.FUND_FLOW_BILLS.getType();
		} else {
			log.info(">>> 不支持的账单类型");
			return null;
		}
		url = wxPayConfig.getDomain().concat(url).concat("?bill_date=").concat(billRequestVO.getBillDate());
		
		//创建远程Get 请求对象
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Accept", "application/json");
		try {
			//使用wxPayClient发送请求得到响应
			CloseableHttpResponse response = wxPayClient.execute(httpGet);
			
			try {
				
				String bodyAsString = EntityUtils.toString(response.getEntity());
				
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					log.info("成功, 申请账单返回结果 = " + bodyAsString);
				} else if (statusCode == 204) {
					log.info("成功");
				} else {
					log.info("申请账单异常, 响应码 {},申请账单返回结果 {}", statusCode, bodyAsString);
					return null;
				}
				//获取账单下载地址
				Map<String, String> resultMap = JSONObject.parseObject(bodyAsString, HashMap.class);
				return resultMap.get("download_url");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			log.error(">>> 查询账单url 出错", e);
			return null;
		}
	}
	
	@Override
	public String downloadTradeBill(BillRequestVO billRequestVO) {
		log.warn("下载账单接口调用 {}, {}", billRequestVO.getBillDate(), billRequestVO.getType());
		
		//获取账单url地址
		String downloadUrl = queryTradeBill(billRequestVO);
		
		if (StringUtils.isEmpty(downloadUrl)) {
			return null;
		}
		
		//创建远程Get 请求对象
		HttpGet httpGet = new HttpGet(downloadUrl);
		httpGet.addHeader("Accept", "application/json");
		
		try {
			//使用wxPayClient发送请求得到响应
			CloseableHttpResponse response = wxPayNoSignClient.execute(httpGet);
			try {
				
				String bodyAsString = EntityUtils.toString(response.getEntity());
				
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					log.info("成功, 下载账单返回结果 = " + bodyAsString);
				} else if (statusCode == 204) {
					log.info("成功");
				} else {
					throw new RuntimeException("下载账单异常, 响应码 = " + statusCode + ", 下载账单返回结果 = " + bodyAsString);
				}
				return bodyAsString;
			} finally {
				response.close();
			}
		} catch (Exception e) {
			log.error(">>> 下载账单出错 ", e);
			return null;
		}
	}
	
	@Override
	public boolean rsaCheckV1(Map<String, String> params) {
		return false;
	}
	
	/**
	 * 构建退款参数
	 *
	 * @param refundInfo 退单单对象
	 */
	private String buildRefundsParam(RefundInfo refundInfo) {
		// 请求body参数
		Map<String, Object> paramsMap = new HashMap(8);
		paramsMap.put("out_trade_no", refundInfo.getOrderNo());
		paramsMap.put("out_refund_no", refundInfo.getRefundNo());
		paramsMap.put("reason", refundInfo.getReason());
		//退款通知地址
		paramsMap.put("notify_url", wxPayConfig.getNotifyDomain()
				.concat(WxNotifyType.REFUND_NOTIFY.getType()));
		Map<String, Object> amountMap = new HashMap();
		amountMap.put("refund", PayUtil.getFee(refundInfo.getRefund()));
		amountMap.put("total", PayUtil.getFee(refundInfo.getTotalFee()));
		//退款币种
		amountMap.put("currency", "CNY");
		paramsMap.put("amount", amountMap);
		return JSONObject.toJSONString(paramsMap);
	}
	
	/**
	 * 构建取消的参数
	 */
	private String buildCancelParam() {
		// 请求body参数
		Map<String, Object> paramsMap = new HashMap(4);
		paramsMap.put("mchid", wxPayConfig.getMchId());
		
		return JSONObject.toJSONString(paramsMap);
	}
	
	/**
	 * 构建的参数形式为:
	 * <p>
	 * {"description":"Java课程","amount":{"total":1,"currency":"CNY"},"mchid":"1558950191",
	 * "out_trade_no":"01564779698500706304",
	 * "notify_url":"https://77ea-221-239-177-21.ngrok.io/api/wx-pay/native/notify","appid":"wx74862e0dfcf69954"
	 * }
	 *
	 * @param orderInfo 订单对象
	 */
	private String buildNativeParam(OrderInfo orderInfo) {
		// 请求body参数
		Map<String, Object> paramsMap = new HashMap(8);
		paramsMap.put("appid", wxPayConfig.getAppId());
		paramsMap.put("mchid", wxPayConfig.getMchId());
		paramsMap.put("description", orderInfo.getTitle());
		paramsMap.put("out_trade_no", orderInfo.getOrderNo());
		paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
		
		Map<String, Object> amountMap = new HashMap();
		amountMap.put("total", PayUtil.getFee(orderInfo.getTotalFee()));
		amountMap.put("currency", "CNY");
		paramsMap.put("amount", amountMap);
		
		return JSONObject.toJSONString(paramsMap);
	}
	
	
}
