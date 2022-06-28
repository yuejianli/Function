package top.yueshushu.learn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import top.yueshushu.learn.business.UserBusiness;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-27
 */
@RestController
public class RetryController {
	
	@Resource
	private UserBusiness userBusiness;
	
	@GetMapping("/noRetry")
	public String noRetry() {
		try {
			userBusiness.noRetry();
			return "运行成功";
		} catch (Exception e) {
			return "运行失败";
		}
	}
	
	
	@GetMapping("/retry")
	public String retry() {
		try {
			userBusiness.retry();
			return "运行成功";
		} catch (Exception e) {
			return "运行失败";
		}
	}
	
	@GetMapping("/propRetry")
	public String propRetry() {
		try {
			userBusiness.propertyRetry();
			return "运行成功";
		} catch (Exception e) {
			return "运行失败";
		}
	}
	
	@GetMapping("/selfRetry")
	public String selfRetry() {
		try {
			userBusiness.selfRetry();
			return "运行成功";
		} catch (Exception e) {
			return "运行失败";
		}
	}
	
	@GetMapping("/listenerRetry")
	public String listenerRetry() {
		try {
			userBusiness.selfRetry();
			return "运行成功";
		} catch (Exception e) {
			return "运行失败";
		}
	}
	
}
