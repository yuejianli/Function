package top.yueshushu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import cn.hutool.extra.spring.EnableSpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 *
 * @author yuejianli
 * @date 2022-08-26
 */
@SpringBootApplication
@Slf4j
@EnableSpringUtil
@EnableScheduling
public class CompensationApp {
	public static void main(String[] args) {
		SpringApplication.run(CompensationApp.class, args);
		log.info(">>> 两个蝴蝶飞，学习业务补偿");
	}
}
