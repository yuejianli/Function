package top.yueshushu.pay.config;

import com.google.common.base.Predicates;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
	
	private static final String SWAGGER_SCAN_BASE_PACKAGE = "top.yueshushu";
	
	private static final String VERSION = "2.0.0";
	
	
	@Bean
	public Docket api() {
		//添加head参数配置start
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.enable(true)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
				.paths(PathSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/error.*")))
				.build();
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Swagger Restful APIS")
				.description("支付系统 api接口文档")
				.termsOfServiceUrl("http://swagger.io/")
				.version(VERSION)
				.build();
	}
}
