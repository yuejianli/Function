package top.yueshushu.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-29
 */
@SpringBootApplication
@Slf4j
@MapperScan(basePackages = "top.yueshushu.pay.mapper")
@EnableScheduling
public class PayApp {
	public static void main(String[] args) {
		SpringApplication.run(PayApp.class, args);
		log.info(">>>>两个蝴蝶飞学习 支付");
	}
}
