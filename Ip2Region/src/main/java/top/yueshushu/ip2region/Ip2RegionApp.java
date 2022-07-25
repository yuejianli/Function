package top.yueshushu.ip2region;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-25
 */
@SpringBootApplication
public class Ip2RegionApp {
	public static void main(String[] args) {
		SpringApplication.run(Ip2RegionApp.class,args);
	}
	
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
