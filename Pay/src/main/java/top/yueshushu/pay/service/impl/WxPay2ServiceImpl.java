package top.yueshushu.pay.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;

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
import top.yueshushu.pay.util.HttpClientUtils;
import top.yueshushu.pay.util.HttpUtils;
import top.yueshushu.pay.vo.BillRequestVO;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@Service("wxPay2Service")
@Slf4j
public class WxPay2ServiceImpl implements IPayService {
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
		HttpClientUtils client = new HttpClientUtils(wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY_V2.getType()));
		
		//组装接口参数
		Map<String, String> params = new HashMap<>();
		//关联的公众号的appid
		params.put("appid", wxPayConfig.getAppId());
		params.put("mch_id", wxPayConfig.getMchId());//商户号
		params.put("nonce_str", WXPayUtil.generateNonceStr());//生成随机字符串
		params.put("body", orderInfo.getTitle());
		params.put("out_trade_no", orderInfo.getOrderNo());
		
		//注意，这里必须使用字符串类型的参数（总金额：分）
		String totalFee = orderInfo.getTotalFee() + "";
		params.put("total_fee", totalFee);
		
		params.put("spbill_create_ip", orderInfo.getIpAddr());
		params.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY_V2.getType()));
		params.put("trade_type", "NATIVE");
		
		//将参数转换成xml字符串格式：生成带有签名的xml格式字符串
		String xmlParams = WXPayUtil.generateSignedXml(params, wxPayConfig.getPartnerKey());
		log.info("\n xmlParams：\n" + xmlParams);
		//将参数放入请求对象的方法体
		client.setXmlParam(xmlParams);
		//使用https形式发送
		client.setHttps(true);
		//发送请求
		client.post();
		//得到响应结果
		String resultXml = client.getContent();
		log.info("\n resultXml：\n" + resultXml);
		//将xml响应结果转成map对象
		Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
		
		//错误处理
		if ("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))) {
			log.error("微信支付统一下单错误 ===> {} ", resultXml);
			throw new RuntimeException("微信支付统一下单错误");
		}
		
		//二维码
		return resultMap.get("code_url");
	}
	
	@Override
	public Map<String, Object> nativeNotify(HttpServletRequest httpServletRequest) {
		//处理通知参数
		String body = HttpUtils.readData(httpServletRequest);
		Map<String, String> returnMap = new HashMap<>();//应答对象
		try {
			//验签
			if (!WXPayUtil.isSignatureValid(body, wxPayConfig.getPartnerKey())) {
				log.error("通知验签失败");
				//失败应答
				returnMap.put("return_code", "FAIL");
				returnMap.put("return_msg", "验签失败");
				String returnXml = WXPayUtil.mapToXml(returnMap);
				return null;
			}
			
			//解析xml数据
			Map<String, String> notifyMap = WXPayUtil.xmlToMap(body);
			//判断通信和业务是否成功
			if (!"SUCCESS".equals(notifyMap.get("return_code")) || !"SUCCESS".equals(notifyMap.get("result_code"))) {
				log.error("失败");
				//失败应答
				returnMap.put("return_code", "FAIL");
				returnMap.put("return_msg", "失败");
				String returnXml = WXPayUtil.mapToXml(returnMap);
				return null;
			}
			
			Map<String, Object> resultMap = new HashMap<>();
			notifyMap.forEach((key, value) -> {
				resultMap.put(key, value);
			});
			return resultMap;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public boolean cancelOrder(String orderNo) {
		return false;
	}
	
	@Override
	public String queryOrder(String orderNo) {
		return null;
	}
	
	@Override
	public String refunds(RefundInfo refundInfo) {
		return null;
	}
	
	@Override
	public String queryRefunds(String refundNo) {
		return null;
	}
	
	@Override
	public Map<String, Object> refundsNotify(HttpServletRequest httpServletRequest) {
		return null;
	}
	
	@Override
	public String queryTradeBill(BillRequestVO billRequestVO) {
		return null;
	}
	
	@Override
	public String downloadTradeBill(BillRequestVO billRequestVO) {
		return null;
	}
	
	@Override
	public boolean rsaCheckV1(Map<String, String> params) {
		return false;
	}
}
