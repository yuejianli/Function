package top.yueshushu.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.event.model.MailMessageEvent;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-22
 */
@Component
@Slf4j
public class MailListener implements ApplicationListener<MailMessageEvent> {
	
	@Override
	@Async
	public void onApplicationEvent(MailMessageEvent mailMessageEvent) {
		log.info(">>>>执行发送邮件事件监听的操作,{}", mailMessageEvent.getMessage());
		try {
			TimeUnit.SECONDS.sleep(4);
			log.info(">>>>> 邮件发送成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(">>>邮件事件监听结束");
	}
}
