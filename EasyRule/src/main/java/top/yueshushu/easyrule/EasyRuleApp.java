package top.yueshushu.easyrule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.hutool.extra.spring.EnableSpringUtil;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-29
 */
@SpringBootApplication
// 启用 SpringUtil
@EnableSpringUtil
public class EasyRuleApp {
	public static void main(String[] args) {
		// 运行
		SpringApplication.run(EasyRuleApp.class, args);
	}
}
