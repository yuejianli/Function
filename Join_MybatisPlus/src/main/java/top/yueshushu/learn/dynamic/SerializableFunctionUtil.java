package top.yueshushu.learn.dynamic;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.ReflectLambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.ShadowLambdaMeta;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-11-08
 */

public class SerializableFunctionUtil {
    public SerializableFunctionUtil() {
    }

    public static <T, R> LambdaMeta extract(SerializableFunction<T, R> func) {
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            return new ReflectLambdaMeta((SerializedLambda) ((Method) ReflectionKit.setAccessible(method)).invoke(func));
        } catch (Throwable var2) {
            return new ShadowLambdaMeta(com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda.extract(func));
        }
    }

    public static <T, R> String getClassFieldName(SerializableFunction<T, R> function) {
        LambdaMeta meta = extract(function);
        String classFieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        return classFieldName;
    }

    public static <T, R> String getSignatureKey(SerializableFunction<T, R> function) {
        LambdaMeta meta = extract(function);
        String classFieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        return meta.getInstantiatedClass().getName() + ":" + classFieldName;
    }

    public static String getSignatureKey(Field field) {
        String className = field.getDeclaringClass().getName();
        return className + ":" + field.getName();
    }

    public static String getSignatureKey(String className, String fieldName) {
        return className + ":" + fieldName;
    }

    public static <T> List<String> getSignatureKey(SerializableFunction<T, ?>... returnFields) {
        if (returnFields != null && returnFields.length != 0) {
            List<String> signatureKeys = new ArrayList(returnFields.length);
            SerializableFunction[] var2 = returnFields;
            int var3 = returnFields.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                SerializableFunction<T, ?> returnField = var2[var4];
                String key = getSignatureKey(returnField);
                signatureKeys.add(key);
            }

            return signatureKeys;
        } else {
            return null;
        }
    }

    public static String getDbSignatureKeyByVoFuncKey(Class dbClass, String voFuncKey) {
        return dbClass.getName() + ":" + voFuncKey.split(":")[1];
    }
}
