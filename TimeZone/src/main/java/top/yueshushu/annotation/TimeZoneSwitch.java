package top.yueshushu.annotation;

import top.yueshushu.entity.TimeSwitchScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时区转换，应用在 Controller 层方法，与注解 {@link TimeZoneField} 配合使用
 * <p>
 * 参数 {@code scope()}，参数类型为{@link TimeSwitchScope}:
 * 参数类型默认为 {@code scope() = TimeSwitchScope.DEFAULT}，默认对输入输出参数全部做时区转换，
 * 设置 {@code scope() = TimeSwitchScope.INPUT_PARAM_ONLY} 仅对 <strong>输入参数</strong> 做时区转换
 * 设置 {@code scope() = TimeSwitchScope.OUTPUT_PARAM_ONLY} 仅对 <strong>输出参数</strong> 做时区转换
 * <p>
 * 使用方式:
 * 1、首先需要在需要时区转换的 {@code Controller} 方法上添加 {@link TimeZoneSwitch 注解}，并选择时区转换范围；
 * 2、切面会扫描接口的请求参数体和接口响应体中定义的 {@link String}、{@link java.util.Date}、{@link java.time.LocalDateTime}
 * 类型日期，若参数体中有需要时区转换的 <p>聚合参数</p>，需在此 <p>聚合参数</p> 上添加 {@link TimeZoneField} 注解
 *
 * @author wanyanhw
 * @date 2022/3/21 10:47
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TimeZoneSwitch {
    /**
     * 默认转换的范围 TimeSwitchScope.DEFAULT
     */
    TimeSwitchScope scope() default TimeSwitchScope.DEFAULT;
}
