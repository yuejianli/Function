package top.yueshushu.pay.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.ScheduledUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@Component
@Slf4j
public class WxPayBean {
	@Resource
	private WxPayConfig wxPayConfig;
	
	/**
	 * 获取商户的私钥文件
	 *
	 * @param fileName 文件路径
	 */
	PrivateKey getPrivateKey(String fileName) {
		
		try {
			return PemUtil.loadPrivateKey(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("私钥文件不存在或者解析不正确", e);
		}
	}
	
	/**
	 * 获取签名验证
	 */
	@Bean
	public ScheduledUpdateCertificatesVerifier getVerifier() {
		log.info("获取签名验证器");
		
		//1. 获取商户私钥
		
		PrivateKey privateKey = getPrivateKey(wxPayConfig.getPrivateKeyPath());
		
		//2. 构建私钥 签名对象  PrivateKeySigner
		
		PrivateKeySigner privateKeySigner = new PrivateKeySigner(wxPayConfig.getMchSerialNo(), privateKey);
		
		//3. 构建身份认证对象  WechatPay2Credentials
		
		WechatPay2Credentials wechatPay2Credentials = new WechatPay2Credentials(wxPayConfig.getMchId(), privateKeySigner);
		
		//4. 使用定时更新的签名验证器, 不需要传入证书
		
		return new ScheduledUpdateCertificatesVerifier(wechatPay2Credentials, wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * 获取http 请求对象, 验证签名
	 *
	 * @param scheduledUpdateCertificatesVerifier 签名验证对象
	 */
	@Bean(name = "wxPayClient")
	public CloseableHttpClient getWxPayClient(ScheduledUpdateCertificatesVerifier scheduledUpdateCertificatesVerifier) {
		log.info(">>>获取 httpClient 验证签名 请求对象");
		
		//1. 获取商户私钥
		PrivateKey privateKey = getPrivateKey(wxPayConfig.getPrivateKeyPath());
		//2. 构建 WechatPayHttpClientBuilder
		WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
				.withMerchant(wxPayConfig.getMchId(), wxPayConfig.getMchSerialNo(), privateKey)
				.withValidator(new WechatPay2Validator(scheduledUpdateCertificatesVerifier));
		//... 可以配置其它的各种参数，定制化 HttpClient
		//3. 构建对象
		CloseableHttpClient closeableHttpClient = builder.build();
		
		return closeableHttpClient;
	}
	
	/**
	 * 获取http 请求对象, 不验证签名
	 */
	@Bean(name = "wxPayNoSignClient")
	public CloseableHttpClient getWxPayNoSignClient() {
		log.info(">>>获取 httpClient 不验证签名 请求对象");
		
		//1. 获取商户私钥
		PrivateKey privateKey = getPrivateKey(wxPayConfig.getPrivateKeyPath());
		//2. 构建 WechatPayHttpClientBuilder
		WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
				.withMerchant(wxPayConfig.getMchId(), wxPayConfig.getMchSerialNo(), privateKey)
				.withValidator((closeableHttpResponse) -> true);
		//... 可以配置其它的各种参数，定制化 HttpClient
		//3. 构建对象
		CloseableHttpClient closeableHttpClient = builder.build();
		return closeableHttpClient;
	}
}
