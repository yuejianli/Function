package top.yueshushu.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@Configuration
@Data
//读取配置文件
@PropertySource("classpath:wxpay.properties")
//读取节点
@ConfigurationProperties(prefix = "wxpay")
public class WxPayConfig {
	/**
	 * 商户号
	 */
	private String mchId;
	/**
	 * 商户API证书序列号
	 */
	private String mchSerialNo;
	/**
	 * 商户私钥文件
	 */
	private String privateKeyPath;
	/**
	 * API v3 密钥
	 */
	private String apiV3Key;
	/**
	 * APPID
	 */
	private String appId;
	/**
	 * 微信服务器地址
	 */
	private String domain;
	/**
	 * 接收结果通知地址
	 */
	private String notifyDomain;
	/**
	 * API v2 密钥
	 */
	private String partnerKey;
}
