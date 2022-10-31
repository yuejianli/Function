package top.yueshushu.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.yueshushu.annotation.TimeZoneField;
import top.yueshushu.annotation.TimeZoneSwitch;
import top.yueshushu.comment.TimeZoneComponent;
import top.yueshushu.common.BasePage;
import top.yueshushu.common.Global;
import top.yueshushu.common.ResponseResult;
import top.yueshushu.entity.TimeSwitchScope;
import top.yueshushu.enumtype.ParamSourceEnum;
import top.yueshushu.utils.DateUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * 时区转换切面
 * 1、将输入参数中的时间，转换成系统时区的时间；
 * 2、将输出参数中的时间，转换成商家时区时间
 *
 * @author yuejianli
 * @date 2022-3-21
 * @since 1.3.0
 */
@Slf4j
@Aspect
@Component
public class TimeZoneAspect {

    @Resource
    private TimeZoneComponent timeZoneComponent;

    @Pointcut("execution(public * top.yueshushu.controller.*.*(..))")
    public void point() {
    }

    /**
     * 重置响应参数的时间字段
     *
     * @param result           响应结果
     * @param sysTimeZone      系统时区
     * @param merchantTimeZone 商家时区
     */
    public static void resetResponseTimeField(Object result, String sysTimeZone, String merchantTimeZone) {
        if (result == null) {
            return;
        }
        if (result instanceof ResponseResult) {
            ResponseResult responseResult = (ResponseResult) result;
            Object data = responseResult.getData();
            if (data == null) {
                return;
            }
            if (data instanceof BasePage) {
                // 处理分页的数据
                BasePage basePage = (BasePage) data;
                for (Object ele : basePage.getList()) {
                    resetTimeField(sysTimeZone, merchantTimeZone, ele, ParamSourceEnum.OUTPUT.name());
                }
            } else {
                resetTimeField(sysTimeZone, merchantTimeZone, data, ParamSourceEnum.OUTPUT.name());
            }
            return;
        }
        resetTimeField(sysTimeZone, merchantTimeZone, result, ParamSourceEnum.OUTPUT.name());
    }

    /**
     * 处理请求参数
     *
     * @param requestArgs      请求参数
     * @param merchantTimeZone 商家时区
     * @param sysTimeZone      系统时区
     */
    private void beforeHandle(Object[] requestArgs, String merchantTimeZone, String sysTimeZone) {
        for (Object requestArg : requestArgs) {
            resetTimeField(merchantTimeZone, sysTimeZone, requestArg, ParamSourceEnum.INPUT.name());
        }
    }

    /**
     * 转换时间并完成数据替换
     *
     * @param srcTimeZone 源时区
     * @param desTimeZone 目标时区
     * @param sourceObj   待操作对象
     * @param paramOpType 参数传输类型
     * @see ParamSourceEnum
     */
    private static void resetTimeField(String srcTimeZone, String desTimeZone, Object sourceObj, String paramOpType) {
        Class<?> sourceObjClass = sourceObj.getClass();
        // 处理数组类型参数 List<?> 的数据数据
        if (sourceObj instanceof List) {
            for (Object element : ((List) sourceObj)) {
                resetTimeField(srcTimeZone, desTimeZone, element, paramOpType);
            }
        }

        try {
            // 转换可以直接转换的数据
            Object transFormValue = transform(sourceObjClass, sourceObj, srcTimeZone, desTimeZone);
            transformLog(sourceObjClass, sourceObj, transFormValue, null, null, srcTimeZone, desTimeZone, paramOpType);
            return;
        } catch (Exception ignored) {
        }

        Field[] fields = sourceObjClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class fieldType = field.getType();
            String fieldName = field.getName();
            Object fieldValue;
            try {
                fieldValue = field.get(sourceObj);
            } catch (IllegalAccessException e) {
                log.error("TimeZoneAspect get field {} fail", fieldName, e);
                continue;
            }

            if (fieldValue == null) {
                continue;
            }

            TimeZoneField timeZoneField = field.getAnnotation(TimeZoneField.class);
            if (timeZoneField != null) {
                if (fieldType == List.class) {
                    for (Object element : ((List) fieldValue)) {
                        resetTimeField(srcTimeZone, desTimeZone, element, paramOpType);
                    }
                } else {
                    resetTimeField(srcTimeZone, desTimeZone, fieldValue, paramOpType);
                }
            }

            Object transFormValue;
            try {
                transFormValue = transform(fieldType, fieldValue, srcTimeZone, desTimeZone);
            } catch (Exception e) {
                continue;
            }

            transformLog(sourceObjClass, fieldValue, transFormValue, fieldType.toString(), fieldName, srcTimeZone, desTimeZone, paramOpType);

            // 重新设置新值
            try {
                field.set(sourceObj, transFormValue);
            } catch (IllegalAccessException e) {
                log.error("TimeZoneAspect conversion exception: field {} can not reset", fieldName, e);
            }
        }
    }

    /**
     * 时间格式化
     *
     * @param time     时间
     * @param timeZone 时区
     * @return String
     */
    private static String timeFormat(Object time, TimeZone timeZone) {
        if (time == null) {
            return "";
        }
        String format = time.toString();
        if (time instanceof Date) {
            try {
                format = DateUtils.formatTimeZone((Date) time, timeZone);
            } catch (Exception e) {
                log.warn("TimeZoneAspect date {} format exception", time);
            }
        } else if (time instanceof LocalDateTime) {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.FORMAT);
                return dateTimeFormatter.format((LocalDateTime) time);
            } catch (Exception e) {
                log.warn("TimeZoneAspect localDateTime {} format exception", time);
            }
        }
        return format;
    }

    /**
     * 时间转换
     *
     * @param fieldType   参数类型
     * @param fieldValue  参数内容
     * @param srcTimeZone 源时区
     * @param desTimeZone 目标时区
     * @return Object
     * @throws Exception e
     */
    private static Object transform(Class fieldType, Object fieldValue, String srcTimeZone, String desTimeZone) throws Exception {
        Object returnValue;
        if (fieldType == String.class) {
            returnValue = DateUtils.transformString((String) fieldValue, srcTimeZone, desTimeZone);
        } else if (fieldType == Date.class) {
            returnValue = DateUtils.transformDate((Date) fieldValue, srcTimeZone, desTimeZone);
        } else if (fieldType == LocalDateTime.class) {
            returnValue = DateUtils.transformLocalDateTime((LocalDateTime) fieldValue, srcTimeZone, desTimeZone);
        } else {
            throw new Exception();
        }
        return returnValue;
    }

    /**
     * 记录转换日志信息
     *
     * @param sourceObjectClass 对象类型
     * @param originalValue     原数据值
     * @param afterValue        转换后的新值
     * @param fieldType         字段类型
     * @param fieldName         字段名称
     * @param srcTimeZone       源时区
     * @param desTimeZone       目标时区
     * @param paramOpType       参数操作类型
     * @see ParamSourceEnum
     */
    private static void transformLog(Class sourceObjectClass, Object originalValue, Object afterValue, String fieldType,
                                     String fieldName, String srcTimeZone, String desTimeZone, String paramOpType) {
        String originalTimeValue = timeFormat(originalValue, TimeZone.getTimeZone("GMT" + srcTimeZone));
        String parsedTimeValue = timeFormat(afterValue, TimeZone.getTimeZone("GMT" + desTimeZone));
        log.info("{} class [{}] type [{}] fieldName [{}], before: {}, after: {}", paramOpType,
                sourceObjectClass.getName(), fieldType, fieldName, originalTimeValue, parsedTimeValue);
    }

    /**
     * 设置字段可见
     *
     * @param field 字段
     * @return Field
     */
    private static Field getAccessibleField(Field field) {
        field.setAccessible(true);
        return field;
    }

    @Around("point()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        TimeZoneSwitch timeZoneSwitch = method.getAnnotation(TimeZoneSwitch.class);
        if (timeZoneSwitch == null) {
            return joinPoint.proceed();
        }
        // 系统时区
        String sysTimeZone = TimeZone.getDefault().toZoneId().getRules().toString().split("=")[1].split(":")[0];
        // 商家时区
        //  String targetTimeZone = timeZoneComponent.getCacheMerchantTimeZone(Global.getUser().getMerchantId(), sysTimeZone);

        // 使用客户端的时区
        // 形式是  GMT:+8:00
        String targetTimeZone = Optional.ofNullable(Global.getTransfer("timezone")).map(n -> n.toString().substring(4)).orElse(sysTimeZone);

        TimeSwitchScope scope = timeZoneSwitch.scope();
        if (TimeSwitchScope.DEFAULT.equals(scope) || TimeSwitchScope.INPUT_PARAM_ONLY.equals(scope)) {
            beforeHandle(joinPoint.getArgs(), targetTimeZone, sysTimeZone);
        }
        Object result = joinPoint.proceed();
        if (TimeSwitchScope.DEFAULT.equals(scope) || TimeSwitchScope.OUTPUT_PARAM_ONLY.equals(scope)) {
            afterHandle(result, sysTimeZone, targetTimeZone);
        }
        return result;
    }

    /**
     * 处理返回参数
     *
     * @param result           返回参数
     * @param sysTimeZone      系统时区
     * @param merchantTimeZone 商家时区
     */
    private void afterHandle(Object result, String sysTimeZone, String merchantTimeZone) {
        try {
            resetResponseTimeField(result, sysTimeZone, merchantTimeZone);
        } catch (Exception e) {
            log.error("TimeZoneAspect handle result error", e);
        }
    }

//    /**
//     * 缓存数据对象信息
//     * @param fieldName 字段名称
//     * @param obj 数据对象
//     */
//    private void setCacheObject(String fieldName, Object obj) {
//        Map<String, Object> fieldObjectMap = CACHE_OBJECT.get();
//        if (fieldObjectMap == null) {
//            fieldObjectMap = new HashMap<>(2);
//        }
//        fieldObjectMap.put(fieldName, obj);
//        CACHE_OBJECT.set(fieldObjectMap);
//    }
//
//    /**
//     * 清空缓存对象
//     * @param fieldName 字段名称
//     */
//    private void releaseCacheField(String fieldName) {
//        Map<String, Object> fieldObjectMap = CACHE_OBJECT.get();
//        if (fieldObjectMap != null) {
//            fieldObjectMap.remove(fieldName);
//        }
//    }
//
//    /**
//     * 清空缓存对象
//     */
//    private void releaseAllCacheField() {
//        CACHE_OBJECT.remove();
//    }
}
