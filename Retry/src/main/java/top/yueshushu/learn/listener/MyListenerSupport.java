package top.yueshushu.learn.listener;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义重试监听器
 *
 * @author yuejianli
 * @date 2022-06-28
 */
@Slf4j
public class MyListenerSupport extends RetryListenerSupport {
	
	/**
	 * 最后关闭的时候， 执行一次
	 */
	
	@Override
	public <T, E extends Throwable> void close(RetryContext context,
											   RetryCallback<T, E> callback, Throwable throwable) {
		log.info("onClose,关闭重试");
		super.close(context, callback, throwable);
	}
	
	/**
	 * 每一次执行失败时调用 ，可能会执行多次。
	 */
	@Override
	public <T, E extends Throwable> void onError(RetryContext context,
												 RetryCallback<T, E> callback, Throwable throwable) {
		log.info("onError,重试的结果，最终是失败的");
		super.onError(context, callback, throwable);
	}
	
	/**
	 * 最开始执行的时候， 执行一次
	 */
	@Override
	public <T, E extends Throwable> boolean open(RetryContext context,
												 RetryCallback<T, E> callback) {
		log.info("onOpen,开始重试");
		return super.open(context, callback);
	}
}
