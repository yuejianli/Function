package top.yueshushu;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.service.CompensationService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-26
 */
@Component
@Slf4j
public class Job {
	@Resource
	private CompensationService compensationService;
	
	@Scheduled(cron = "0/5 * * * * ?")
	public void runService() {
		String result = compensationService.autoRetry();
		// log.info(">>>信息:{}",result);
	}
}
