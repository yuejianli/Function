#H1 作用
主要用于 Retry 重试处理。 

1. 添加依赖

    <!--添加重试的依赖-->
        <dependency>
            <groupId>org.springframework.retry</groupId>
            <artifactId>spring-retry</artifactId>
        </dependency>
        
 2. 开启 Retry 注解  @EnableRetry
 
 @SpringBootApplication
 // 开启注解
 @EnableRetry
 public class RetryApp {
 	public static void main(String[] args) {
 		SpringApplication.run(RetryApp.class,args);
 	}
 }       
 
 
 3. 在接口方法上 配置使用
 
 /**
 	
 	 value 表示 何种异常时，进行重试.
 	 maxAttempts 重试的次数，默认是 3次
 	 
 	 backoff 表示 第一次调用失败时，执行的策略。
 	  delay 表示， 第一次隔多长时间 2s 后重新调用.
 	   multiplier 表示 重试的延迟倍数， 刚开始是 2s, 后来是 2*2 =4 秒， 再后来是 4*2 = 8s
 	 
 	 @Recover 表示重试一直失败，到了最大重试次数后调用
 	 
 	 */
 	
 	@Override
 	@Retryable(value = Exception.class,maxAttempts = 3,backoff = @Backoff(delay = 2000,multiplier = 2))
 	public void retry() throws Exception{
 		Random random = new Random(System.currentTimeMillis());
 		int nextNum = random.nextInt(10);
 		if (nextNum >=3){
 			log.error(">>>> 运行时间是:{},超时失败",nextNum);
 			throw new Exception("运行超时");
 		}else{
 			log.info(">>>>>>>运行成功,运动时间是:{}",nextNum);
 		}
 	}
 
 
 
 4. 配置 兜底策略
 
 	/**
 	 重试一直失败 调用 。
 	 如果一直失败，则抛出异常。 这样，外部才可以捕获到
 	 */
 	@Recover
 	public void retryUtilException () throws Exception{
 		log.error(">>>>>重试一直失败");
 		throw new Exception("奶奶的，运行一直失败");
 	}	