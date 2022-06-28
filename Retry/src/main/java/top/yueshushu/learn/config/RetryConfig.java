package top.yueshushu.learn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import top.yueshushu.learn.listener.MyListenerSupport;

/**
 * 自定义监听器
 *
 * @author yuejianli
 * @date 2022-06-28
 */
@Configuration
public class RetryConfig {
	
	@Bean
	public RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();
		
		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		//设置重试回退
		fixedBackOffPolicy.setBackOffPeriod(2000L);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
		
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		//设置 基本的，最大次数等。
		retryPolicy.setMaxAttempts(3);
		retryTemplate.setRetryPolicy(retryPolicy);
		
		// 注册监听器
		retryTemplate.registerListener(new MyListenerSupport());
		
		return retryTemplate;
	}
}
