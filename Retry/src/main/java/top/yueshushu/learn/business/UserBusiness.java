package top.yueshushu.learn.business;

/**
 * 重试策略 业务处理
 *
 * @author yuejianli
 * @date 2022-06-27
 */

public interface UserBusiness {
	/**
	 * 没有重试策略
	 */
	void noRetry() throws Exception;
	
	/**
	 * 有重试策略
	 */
	void retry() throws Exception;
	
	
	/**
	 * 配置文件形式，重试策略
	 */
	void propertyRetry() throws Exception;
	
	
	/**
	 * 自定义的 RetryTemplate 设置的策略
	 */
	void selfRetry() throws Exception;
	
	
	/**
	 * 监听器 RetryTemplate 设置的策略
	 */
	void listenerRetry() throws Exception;
}
