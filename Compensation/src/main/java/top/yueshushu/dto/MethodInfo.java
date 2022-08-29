package top.yueshushu.dto;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import top.yueshushu.anno.Compensate;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-26
 */
@Data
public class MethodInfo {
	/*** 切入点信息*/
	private JoinPoint joinPoint;
	/*** 方法签名*/
	private MethodSignature signature;
	/*** 方法信息*/
	private Method method;
	/*** 类信息*/
	private Class<?> targetClass;
	/*** 参数信息*/
	private Object[] args;
	/*** 参数信息String*/
	private String jsonArgs;
	/*** 类名*/
	private String className;
	/*** 方法名*/
	private String methodName;
	/**
	 * 重试次数
	 */
	private Integer retryCount;
	
	public MethodInfo(JoinPoint joinPoint) {
		this.joinPoint = joinPoint;
	}
	
	public void init() {
		this.signature = (MethodSignature) joinPoint.getSignature();
		this.method = signature.getMethod();
		this.methodName = method.getName();
		this.targetClass = method.getDeclaringClass();
		this.className = targetClass.getName();
		this.args = joinPoint.getArgs();
		if (ArrayUtil.isEmpty(args)) {
			this.jsonArgs = "";
		} else {
			if (this.args.length == 1) {
				this.jsonArgs = this.args[0] + "";
			} else {
				this.jsonArgs = StrUtil.join(",", args);
			}
		}
		Compensate compensate = method.getAnnotation(Compensate.class);
		this.retryCount = compensate.retryCount();
		
	}
}
