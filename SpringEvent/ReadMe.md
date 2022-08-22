使用 Spring Event

1. 添加依赖 springboot 已经携带

同步处理:

1. 构建事件， 继承 ApplicationEvent

~~~java
public class OrderEvent extends ApplicationEvent {
	private String message;
	
	public OrderEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	public OrderEvent(Object source) {
		super(source);
	}
	
	public String getMessage() {
		return message;
	}
}
~~~

2. 根据事件，进行监听。 实现 ApplicationListener

~~~java

@Slf4j
@Component
public class OrderListener implements ApplicationListener<OrderEvent> {
	
	@Override
	public void onApplicationEvent(OrderEvent event) {
		//执行操作
		log.info(">>> 执行事件监听开始,调用参数是:{}", event.getMessage());
		try {
			//执行具体的业务处理逻辑
			TimeUnit.SECONDS.sleep(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(">>>>>>执行事件监听结束");
	}
}
~~~

3. 业务处理，其中需要添加 Event 事件。 会自动调用 Listener 监听

~~~java

@Service
@Slf4j
public class OrderEventService {
	
	@Resource
	private ApplicationContext applicationContext;
	
	/**
	 处理事件
	 */
	public void handlerOrder(String message) {
		log.info(">>>>开始处理订单,订单信息是:{}", message);
		applicationContext.publishEvent(new OrderEvent(this, message));
		log.info(">>>> 调用订单程序成功,任务结束");
	}
}
~~~

4. 业务测试

~~~java

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderEventTest {
	@Resource
	private OrderEventService orderEventService;
	
	@Test
	public void syncTest() {
		orderEventService.handlerOrder("岳泽霖购买护肤品");
	}
}
~~~

5. 测试结果为:

~~~java
/**
 同步测试
 2022-08-22 10:45:03.571  INFO 13836 --- [           main] t.y.event.build.OrderEventService        : >>>>开始处理订单,订单信息是:岳泽霖购买护肤品
 2022-08-22 10:45:03.572  INFO 13836 --- [           main] t.y.event.listener.OrderListener         : >>> 执行事件监听开始,调用参数是:岳泽霖购买护肤品
 2022-08-22 10:45:06.572  INFO 13836 --- [           main] t.y.event.listener.OrderListener         : >>>>>>执行事件监听结束
 2022-08-22 10:45:06.573  INFO 13836 --- [           main] t.y.event.build.OrderEventService        : >>>> 调用订单程序成功,任务结束
 2022-08-22 10:45:06.582  INFO 13836 --- [extShutdownHook] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
 */
~~~

异步事件处理:

1. 添加 MailMessageEvent 异步 Event

~~~java
public class MailMessageEvent extends ApplicationEvent {
	private String message;
	
	public MailMessageEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
~~~

2. 事件 Listener 监听

~~~java

@Component
@Slf4j
public class MailListener implements ApplicationListener<MailMessageEvent> {
	
	@Override
	public void onApplicationEvent(MailMessageEvent mailMessageEvent) {
		log.info(">>>>执行发送邮件事件监听的操作,{}", mailMessageEvent.getMessage());
		try {
			TimeUnit.SECONDS.sleep(4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(">>>邮件事件监听结束");
	}
}
~~~

3. 业务注入 Event

~~~java
    public void handlerEmail(String message){
		log.info(">>>>开始处理订单,订单信息是:{}",message);
		//处理具体的业务信息
		handlerOrder(message);
		//发送邮件程序
		applicationContext.publishEvent(new MailMessageEvent(this,"发送邮件"));
		log.info(">>>> 调用订单程序成功,任务结束");
		}
~~~

4. 测试处理

~~~java
@Test
public void asyncTest(){
		orderEventService.handlerEmail("岳泽霖购买护肤品");
		try{
		TimeUnit.SECONDS.sleep(12);
		}catch(Exception e){
		
		}
		}
~~~

执行，并没有异步处理.

~~~java
     同步时:
		2022-08-22 10:45:31.218INFO 13404---[main]t.y.event.build.OrderEventService:>>>>开始处理订单,订单信息是:岳泽霖购买护肤品
		2022-08-22 10:45:31.219INFO 13404---[main]t.y.event.build.OrderEventService:>>>>开始处理订单,订单信息是:岳泽霖购买护肤品
		2022-08-22 10:45:31.219INFO 13404---[main]t.y.event.listener.OrderListener:>>>执行事件监听开始,调用参数是:岳泽霖购买护肤品
		2022-08-22 10:45:34.221INFO 13404---[main]t.y.event.listener.OrderListener:>>>>>>执行事件监听结束
		2022-08-22 10:45:34.221INFO 13404---[main]t.y.event.build.OrderEventService:>>>>调用订单程序成功,任务结束
		2022-08-22 10:45:34.222INFO 13404---[main]t.yueshushu.event.listener.MailListener:>>>>执行发送邮件事件监听的操作,发送邮件
		2022-08-22 10:45:38.232INFO 13404---[main]t.yueshushu.event.listener.MailListener:>>>邮件事件监听结束
		2022-08-22 10:45:38.233INFO 13404---[main]t.y.event.build.OrderEventService:>>>>调用订单程序成功,任务结束
~~~

添加异步注解:

启用 @EnableAsync 注解

~~~java

@SpringBootApplication
@EnableAsync
@Slf4j
public class SpringEventApp {
	public static void main(String[] args) {
		SpringApplication.run(SpringEventApp.class, args);
		log.info(">> 岳叔叔学习 Spring Event ");
	}
}
~~~

在对应的方法上，添加 @Async 表示异步

~~~java

@Component
@Slf4j
public class MailListener implements ApplicationListener<MailMessageEvent> {
	
	@Override
	@Async
	public void onApplicationEvent(MailMessageEvent mailMessageEvent) {
		log.info(">>>>执行发送邮件事件监听的操作,{}", mailMessageEvent.getMessage());
		try {
			TimeUnit.SECONDS.sleep(4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(">>>邮件事件监听结束");
	}
}
~~~

执行，会进行异步处理.

~~~java
 2022-08-22 10:47:43.438INFO 25872---[main]t.y.event.build.OrderEventService:>>>>开始处理订单,订单信息是:岳泽霖购买护肤品
		2022-08-22 10:47:43.439INFO 25872---[main]t.y.event.build.OrderEventService:>>>>开始处理订单,订单信息是:岳泽霖购买护肤品
		2022-08-22 10:47:43.440INFO 25872---[main]t.y.event.listener.OrderListener:>>>执行事件监听开始,调用参数是:岳泽霖购买护肤品
		2022-08-22 10:47:46.447INFO 25872---[main]t.y.event.listener.OrderListener:>>>>>>执行事件监听结束
		2022-08-22 10:47:46.447INFO 25872---[main]t.y.event.build.OrderEventService:>>>>调用订单程序成功,任务结束
		2022-08-22 10:47:46.450INFO 25872---[main]t.y.event.build.OrderEventService:>>>>调用订单程序成功,任务结束
		2022-08-22 10:47:46.450INFO 25872---[task-1]t.yueshushu.event.listener.MailListener:>>>>执行发送邮件事件监听的操作,发送邮件
		2022-08-22 10:47:50.464INFO 25872---[task-1]t.yueshushu.event.listener.MailListener:>>>邮件事件监听结束
~~~


