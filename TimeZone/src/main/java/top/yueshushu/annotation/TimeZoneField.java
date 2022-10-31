package top.yueshushu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时区转换，注解在 List 属性或聚合属性上，整体使用方法见 {@link TimeZoneSwitch} 方法描述信息
 *
 * @author wanyanhw
 * @date 2022/3/21 10:47
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TimeZoneField {
}
