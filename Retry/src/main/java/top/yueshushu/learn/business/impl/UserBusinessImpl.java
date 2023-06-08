package top.yueshushu.learn.business.impl;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.learn.business.UserBusiness;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-27
 */
@Service
@Slf4j
public class UserBusinessImpl implements UserBusiness {
	@Resource
	private RetryTemplate retryTemplate;
	
	@Override
	public void noRetry() throws Exception {
		Random random = new Random(System.currentTimeMillis());
		int nextNum = random.nextInt(10);
		if (nextNum >= 3) {
			log.error(">>>> 运行时间是:{},超时失败", nextNum);
			throw new Exception("运行超时");
		} else {
			log.info(">>>>>>>运行成功");
		}
	}
	
	/**
	 * value 表示 何种异常时，进行重试.
	 * maxAttempts 重试的次数，默认是 3次
	 * <p>
	 * backoff 表示 第一次调用失败时，执行的策略。
	 * delay 表示， 第一次隔多长时间 2s 后重新调用.
	 * multiplier 表示 重试的延迟倍数， 刚开始是 2s, 后来是 2*2 =4 秒， 再后来是 4*2 = 8s
	 *
	 * @Recover 表示重试一直失败，到了最大重试次数后调用
	 */

	@Override
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
	public void retry() throws Exception {
		Random random = new Random(System.currentTimeMillis());
		int nextNum = random.nextInt(10);
		if (nextNum >= 2) {
			log.error(">>>> 运行时间是:{},超时失败", nextNum);
			throw new Exception("运行超时");
		} else {
			log.info(">>>>>>>运行成功,运动时间是:{}", nextNum);
		}
	}
	
	/**
	 * 使用的是表达式配置处理
	 */
	
	@Override
	@Retryable(value = Exception.class, maxAttemptsExpression = "${retry.maxAttempts}",
			backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
	public void propertyRetry() throws Exception {
		
		Random random = new Random(System.currentTimeMillis());
		int nextNum = random.nextInt(10);
		if (nextNum >= 1) {
			log.error(">>>> 运行时间是:{},超时失败", nextNum);
			throw new Exception("运行超时");
		} else {
			log.info(">>>>>>>运行成功,运动时间是:{}", nextNum);
		}
	}
	
	/**
	 * '
	 * <p>
	 * 最后不会调用 retryUtilException() 方法。
	 */
	@Override
	public void selfRetry() throws Exception {
		try {
			retryTemplate.execute(new RetryCallback<Object, Throwable>() {
				@Override
				public Object doWithRetry(RetryContext context) throws Throwable {
					// 调用 重试方法
					try {
						noRetry();
						return null;
					} catch (Exception e) {
						// 有异常，往上抛出。
						throw e;
					}
				}
			});
		} catch (Throwable e) {
			throw new Exception("重试失败");
		}
	}
	
	@Override
	public void listenerRetry() throws Exception {
		try {
			retryTemplate.execute(new RetryCallback<Object, Throwable>() {
				@Override
				public Object doWithRetry(RetryContext context) throws Throwable {
					// 调用 重试方法
					try {
						noRetry();
						return null;
					} catch (Exception e) {
						// 有异常，往上抛出。
						throw e;
					}
				}
			});
		} catch (Throwable e) {
			throw new Exception("重试失败");
		}
	}
	
	
	/**
	 * 重试一直失败 调用 。
	 * 如果一直失败，则抛出异常。 这样，外部才可以捕获到
	 */
	@Recover
	public void retryUtilException() throws Exception  {
		log.error(">>>>>重试一直失败,执行一个兜底的操作");
		throw new Exception("一直失败,但还是要努力噢");
	}

//	@CircuitBreaker(openTimeout = 10000, maxAttempts =  3,resetTimeout = 3000, value = Exception.class)
//	public void circuit() throws Exception {
//		log.info(">>> 不好了，触发熔断了");
//		throw new Exception("触发了熔断异常");
//	}
}
