package top.yueshushu.pay.config;

import com.alipay.api.*;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-01
 */
@Component
@Slf4j
public class AlipayBean {
	@Resource
	private AlipayPropertiesConfig alipayPropertiesConfig;
	
	@Bean
	public AlipayClient alipayClient() throws AlipayApiException {
		
		AlipayConfig alipayConfig = new AlipayConfig();
		
		//设置网关地址
		alipayConfig.setServerUrl(alipayPropertiesConfig.getGatewayUrl());
		//设置应用Id
		alipayConfig.setAppId(alipayPropertiesConfig.getAppId());
		//设置应用私钥
		alipayConfig.setPrivateKey(alipayPropertiesConfig.getMerchantPrivateKey());
		//设置请求格式，固定值json
		alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);
		//设置字符集
		alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);
		//设置支付宝公钥
		alipayConfig.setAlipayPublicKey(alipayPropertiesConfig.getAlipayPublicKey());
		//设置签名类型
		alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
		//构造client
		AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
		
		return alipayClient;
	}
}
