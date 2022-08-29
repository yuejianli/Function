package top.yueshushu.anno;

import java.lang.annotation.*;

/**
 * <p>
 * 注解
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-26 16:36
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Compensate {
	/**
	 * 补获的异常类型
	 */
	Class<?>[] value() default Exception.class;
	
	/**
	 * 重试的次数，默认是3次。
	 */
	int retryCount() default 3;
	
}
