package top.yueshushu.learn.dynamic;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-11-08
 */

@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {

}
