package top.yueshushu.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-27
 */
@SpringBootApplication
// 开启注解
@EnableRetry
// 引入配置文件
@PropertySource("classpath:retryConfig.properties")
public class RetryApp {
	public static void main(String[] args) {
		SpringApplication.run(RetryApp.class, args);
	}
}
