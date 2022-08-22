package top.yueshushu.event.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import top.yueshushu.event.build.OrderEventService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-22
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderEventTest {
	@Resource
	private OrderEventService orderEventService;
	
	/**
	 * 同步测试
	 * 2022-08-22 10:45:03.571  INFO 13836 --- [           main] t.y.event.build.OrderEventService        : >>>>开始处理订单,订单信息是:岳泽霖购买护肤品
	 * 2022-08-22 10:45:03.572  INFO 13836 --- [           main] t.y.event.listener.OrderListener         : >>> 执行事件监听开始,调用参数是:岳泽霖购买护肤品
	 * 2022-08-22 10:45:06.572  INFO 13836 --- [           main] t.y.event.listener.OrderListener         : >>>>>>执行事件监听结束
	 * 2022-08-22 10:45:06.573  INFO 13836 --- [           main] t.y.event.build.OrderEventService        : >>>> 调用订单程序成功,任务结束
	 * 2022-08-22 10:45:06.582  INFO 13836 --- [extShutdownHook] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
	 */
	@Test
	public void syncTest() {
		orderEventService.handlerOrder("岳泽霖购买护肤品");
	}
	
	/**
	 * 异步测试
	 * 同步时:
	 * 2022-08-22 10:45:31.218  INFO 13404 --- [           main] t.y.event.build.OrderEventService        : >>>>开始处理订单,订单信息是:岳泽霖购买护肤品
	 * 2022-08-22 10:45:31.219  INFO 13404 --- [           main] t.y.event.build.OrderEventService        : >>>>开始处理订单,订单信息是:岳泽霖购买护肤品
	 * 2022-08-22 10:45:31.219  INFO 13404 --- [           main] t.y.event.listener.OrderListener         : >>> 执行事件监听开始,调用参数是:岳泽霖购买护肤品
	 * 2022-08-22 10:45:34.221  INFO 13404 --- [           main] t.y.event.listener.OrderListener         : >>>>>>执行事件监听结束
	 * 2022-08-22 10:45:34.221  INFO 13404 --- [           main] t.y.event.build.OrderEventService        : >>>> 调用订单程序成功,任务结束
	 * 2022-08-22 10:45:34.222  INFO 13404 --- [           main] t.yueshushu.event.listener.MailListener  : >>>>执行发送邮件事件监听的操作,发送邮件
	 * 2022-08-22 10:45:38.232  INFO 13404 --- [           main] t.yueshushu.event.listener.MailListener  : >>>邮件事件监听结束
	 * 2022-08-22 10:45:38.233  INFO 13404 --- [           main] t.y.event.build.OrderEventService        : >>>> 调用订单程序成功,任务结束
	 * 异步时:
	 * 2022-08-22 10:47:43.438  INFO 25872 --- [           main] t.y.event.build.OrderEventService        : >>>>开始处理订单,订单信息是:岳泽霖购买护肤品
	 * 2022-08-22 10:47:43.439  INFO 25872 --- [           main] t.y.event.build.OrderEventService        : >>>>开始处理订单,订单信息是:岳泽霖购买护肤品
	 * 2022-08-22 10:47:43.440  INFO 25872 --- [           main] t.y.event.listener.OrderListener         : >>> 执行事件监听开始,调用参数是:岳泽霖购买护肤品
	 * 2022-08-22 10:47:46.447  INFO 25872 --- [           main] t.y.event.listener.OrderListener         : >>>>>>执行事件监听结束
	 * 2022-08-22 10:47:46.447  INFO 25872 --- [           main] t.y.event.build.OrderEventService        : >>>> 调用订单程序成功,任务结束
	 * 2022-08-22 10:47:46.450  INFO 25872 --- [           main] t.y.event.build.OrderEventService        : >>>> 调用订单程序成功,任务结束
	 * 2022-08-22 10:47:46.450  INFO 25872 --- [         task-1] t.yueshushu.event.listener.MailListener  : >>>>执行发送邮件事件监听的操作,发送邮件
	 * 2022-08-22 10:47:50.464  INFO 25872 --- [         task-1] t.yueshushu.event.listener.MailListener  : >>>邮件事件监听结束
	 */
	@Test
	public void asyncTest() {
		orderEventService.handlerEmail("岳泽霖购买护肤品");
		try {
			TimeUnit.SECONDS.sleep(12);
		} catch (Exception e) {
		
		}
	}
}
