package top.yueshushu.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.util.StringUtil;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.anno.Compensate;
import top.yueshushu.dto.MethodInfo;
import top.yueshushu.entity.CompensationDO;
import top.yueshushu.enumtype.DataFlagEnum;
import top.yueshushu.enumtype.RevokeStatusEnum;
import top.yueshushu.enumtype.RevokeTypeEnum;
import top.yueshushu.service.CompensationService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-26
 */
@Aspect
@Component
@Slf4j
public class CompensationAspect {
	@Resource
	private CompensationService compensationService;
	
	/**
	 * 切点连接点：在MyLog注解的位置切入
	 */
	@Pointcut(value = "(@annotation(top.yueshushu.anno.Compensate))")
	public void pointCut() {
	
	}
	
	/**
	 * 异常发生时的通知
	 *
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "pointCut()", throwing = "e")
	public void doExceptionMyLog(JoinPoint joinPoint, Exception e) {
		try {
			// 1：获取需要的数据，并且进行验证
			Method targetMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
			Compensate compensate = targetMethod.getAnnotation(Compensate.class);
			//异常类型
			Class<?>[] exceptionTypes = compensate.value();
			for (Class<?> exceptionType : exceptionTypes) {
				// exceptionType 是 e.getClass() 的父类或者一样的。
				if (exceptionType.isAssignableFrom(e.getClass())) {
					saveCompensationDO(e, joinPoint);
				}
			}
		} catch (Throwable throwable) {
			// 异常需要抛出。要不然没办法，业务代码依赖这个报错的信息会被干扰。
			log.error(throwable.getMessage(), throwable);
		}
	}
	
	/**
	 * 2： 保存异常数据
	 * 保存的数据，应该先查询下，看数据库中是否存在这样的数据，如果不存在就进行插入。存在啥也不做。
	 *
	 * @param e         异常类
	 * @param joinPoint 切面类
	 */
	public void saveCompensationDO(Exception e, JoinPoint joinPoint) throws JsonProcessingException {
		// 1：获取切面对象的参数
		MethodInfo methodInfo = new MethodInfo(joinPoint);
		methodInfo.init();
		// 2：构建需要补偿的对象
		CompensationDO compensationDO = buildCompensationDO(methodInfo, e);
		// 3: 上面根据的数据，得保证是满足数据的完整性。然后进行数据的储存入库。然后结束。
		if (!compensationService.existIngKey(compensationDO.getBusKey())) {
			compensationService.save(compensationDO);
		}
	}
	
	/**
	 * 2-1: 数据转换
	 *
	 * @param methodInfo 包装过后的数据对象
	 * @return 转换的补偿类
	 */
	private CompensationDO buildCompensationDO(MethodInfo methodInfo, Exception e) {
		// 1：存入通用的参数
		CompensationDO compensationDO = new CompensationDO();
		Annotation[] annotations = methodInfo.getTargetClass().getAnnotations();
		String springBeanName = "";
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(Service.class)) {
				springBeanName = methodInfo.getTargetClass().getAnnotation(Service.class).value();
			}
			if (annotation.annotationType().equals(Component.class)) {
				springBeanName = methodInfo.getTargetClass().getAnnotation(Component.class).value();
			}
		}
		if (StringUtil.isEmpty(springBeanName)) {
			springBeanName = StringUtils.uncapitalize(methodInfo.getTargetClass().getSimpleName());
		}
		compensationDO.setBeanName(springBeanName);
		compensationDO.setClassName(methodInfo.getClassName());
		compensationDO.setMethodName(methodInfo.getMethodName());
		compensationDO.setRevokeStatus(RevokeStatusEnum.ING.getValue());
		compensationDO.setExceptionMessage(e.getMessage());
		compensationDO.setRevokeType(RevokeTypeEnum.AUTO.getValue());
		// 2：处理请求参数类型
		// 一：消息无转换，说明入参是一样的。直接存。 1: 单个->类型 2：多个->数组 3. 无参不处理
		Object[] args = methodInfo.getArgs();
		
		if (ArrayUtil.isEmpty(args)) {
		
		} else if (args.length == 1) {
			//获取方法参数类型数组
			compensationDO.setParamValue(methodInfo.getJsonArgs());
			compensationDO.setParamType(args[0].getClass().getName());
			compensationDO.setParamRealType(args[0].getClass().getName());
		} else {
			Class<?>[] classesReq = methodInfo.getMethod().getParameterTypes();
			StringBuilder stringBuffer = new StringBuilder();
			for (Class<?> aClass : classesReq) {
				stringBuffer.append(aClass.getName()).append(",");
			}
			// 用 ,号进行分隔信息。
			compensationDO.setParamValue(methodInfo.getJsonArgs());
			compensationDO.setParamType(Object[].class.getName());
			compensationDO.setParamRealType(stringBuffer.substring(0, stringBuffer.length() - 1));
		}
		compensationDO.setBusKey(createBusKey(compensationDO));
		compensationDO.setRetryCount(methodInfo.getRetryCount());
		compensationDO.setHasRetryCount(0);
		
		compensationDO.setCreateDate(DateUtil.date());
		compensationDO.setUpdateDate(DateUtil.date());
		compensationDO.setFlag(DataFlagEnum.VALID.getValue());
		return compensationDO;
	}
	
	
	/**
	 * 业务主键 根据类名，方法名，入参生成一个摘要值
	 *
	 * @return 业务主键
	 */
	public String createBusKey(CompensationDO compensationDO) {
		return compensationDO.getBeanName() + compensationDO.getMethodName() + compensationDO.getParamValue() + "";
	}
}
