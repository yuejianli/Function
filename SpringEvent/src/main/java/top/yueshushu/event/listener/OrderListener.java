package top.yueshushu.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.event.model.OrderEvent;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-22
 */
@Slf4j
@Component
public class OrderListener implements ApplicationListener<OrderEvent> {
	
	@Override
	@Async
	public void onApplicationEvent(OrderEvent event) {
		//执行操作
		log.info(">>> 执行事件监听开始,调用参数是:{}", event.getMessage());
		try {
			//执行具体的业务处理逻辑
			TimeUnit.SECONDS.sleep(3);
			log.info(">>> 执行具体的任务完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(">>>>>>执行事件监听结束");
	}
}
