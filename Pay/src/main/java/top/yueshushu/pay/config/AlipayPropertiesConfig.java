package top.yueshushu.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
//加载配置文件
@PropertySource("classpath:alipay-sandbox.properties")
//读取节点
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayPropertiesConfig {
	private String appId;
	private String sellerId;
	private String gatewayUrl;
	private String merchantPrivateKey;
	private String alipayPublicKey;
	private String contentKey;
	private String returnUrl;
	private String notifyUrl;
}
