package top.yueshushu.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-22
 */
@SpringBootApplication
@EnableAsync
@Slf4j
public class SpringEventApp {
	public static void main(String[] args) {
		SpringApplication.run(SpringEventApp.class, args);
		log.info(">> 岳叔叔学习 Spring Event ");
	}
}
