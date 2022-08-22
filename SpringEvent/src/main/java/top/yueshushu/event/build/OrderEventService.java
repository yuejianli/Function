package top.yueshushu.event.build;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.event.model.MailMessageEvent;
import top.yueshushu.event.model.OrderEvent;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-22
 */
@Service
@Slf4j
public class OrderEventService {
	
	@Resource
	private ApplicationContext applicationContext;
	
	/**
	 * 处理事件
	 */
	public void handlerOrder(String message) {
		log.info(">>>>开始处理订单,订单信息是:{}", message);
		applicationContext.publishEvent(new OrderEvent(this, message));
		log.info(">>>> 调用订单程序成功,任务结束");
	}
	
	public void handlerEmail(String message) {
		log.info(">>>>开始处理订单,订单信息是:{}", message);
		//处理具体的业务信息
		handlerOrder(message);
		//发送邮件程序
		applicationContext.publishEvent(new MailMessageEvent(this, "发送邮件"));
		log.info(">>>> 调用订单程序成功,任务结束");
	}
}
