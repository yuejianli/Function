package top.yueshushu.learn.dynamic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xx
 */
@Slf4j
public class AbstractClassFieldHelper {

    //key:class:field
    private static Map<String, DbField> fieldCache = new ConcurrentHashMap<>();

    private static Set<String> defaultIgnoreFieldSet = CollUtil.newHashSet(
            "serialVersionUID"
    );

    void putIfAbsent(String key, DbField value) {
        fieldCache.putIfAbsent(key, value);
    }

    protected <T, R> DbField doGetDbFieldByVoFunctionFromCache(String dbClassName, SerializableFunction<T, R> function, boolean isIgnoreNotFindError) {
        String key = SerializableFunctionUtil.getSignatureKey(dbClassName, SerializableFunctionUtil.getClassFieldName(function));
        if (fieldCache.containsKey(key)) {
            return fieldCache.get(key);
        }

        if (!isIgnoreNotFindError) {
            throw new IllegalStateException("not find from cache, key:" + key);
        }
        return null;
    }

    protected DbField doGetDbFieldByFieldFromCache(String className, Field field, boolean isIgnoreNotFindError) {
        String key = SerializableFunctionUtil.getSignatureKey(field);
        if (fieldCache.containsKey(key)) {
            return fieldCache.get(key);
        }

        if (!isIgnoreNotFindError) {
            throw new IllegalStateException("not find from cache, key:" + key);
        }
        return null;
    }

    protected DbField doGetDbFieldByFieldKeyFromCache(String key, boolean isIgnoreNotFindError) {
        if (fieldCache.containsKey(key)) {
            return fieldCache.get(key);
        }

        if (!isIgnoreNotFindError) {
            throw new IllegalStateException("not find from cache, key:" + key);
        }
        return null;
    }

    /**
     * 获取或者创建db field
     *
     * @param clazz               do类
     * @param ignoreFieldSet      忽略字段
     * @param isCreateWhenNoExist 不存在时是否创建
     * @return
     */
    protected List<DbField> doGetOrCreateAllDbField(Class clazz, Set<String> ignoreFieldSet, boolean isCreateWhenNoExist, boolean isIgnoreNotFindError) {
        List<DbField> dbFieldList = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (defaultIgnoreFieldSet.contains(declaredField.getName()) || CollUtil.contains(ignoreFieldSet, SerializableFunctionUtil.getSignatureKey(declaredField))) {
                //log.debug("{} is ignore", declaredField.getName());
                continue;
            }

            DbField dbField = doGetDbFieldByFieldFromCache(clazz.getName(), declaredField, isIgnoreNotFindError);
            if (dbField == null && isCreateWhenNoExist) {
                dbField = doGetDbField(clazz.getName(), declaredField);
            }
            if (dbField != null && !dbFieldList.contains(dbField)) {
                dbFieldList.add(dbField);
            } else {
                log.error("exist exception: {} {}", declaredField, dbField);
            }
        }

        return dbFieldList;
    }

    protected <T, R> DbField doGetDbFieldByFunction(SerializableFunction<T, R> function) {
        LambdaMeta meta = SerializableFunctionUtil.extract(function);
        String classFieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        Field declaredField;
        try {
            declaredField = meta.getInstantiatedClass().getDeclaredField(classFieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return doGetDbField(meta.getInstantiatedClass().getName(), declaredField);
    }

    private DbField doGetDbField(String className, Field declaredField) {
        String classFieldName = declaredField.getName();
        String tableName, tableFieldName = classFieldName;
        TableField tableField = declaredField.getAnnotation(TableField.class);
        if (tableField != null) {
            tableFieldName = tableField.value();
        }
        TableId tableId = declaredField.getAnnotation(TableId.class);
        if (tableId != null) {
            tableFieldName = tableId.value();
        }

        Class<?> declaringClass = declaredField.getDeclaringClass();
        TableNameAlias tableNameAlias = declaringClass.getAnnotation(TableNameAlias.class);
        if (tableNameAlias != null) {
            tableName = tableNameAlias.value();
        } else {
            TableName tableNameAnnotation = declaringClass.getAnnotation(TableName.class);
            Assert.notNull(tableNameAnnotation);
            tableName = tableNameAnnotation.value();
        }

        Assert.isTrue(classFieldName != null && tableName != null && tableFieldName != null);

        return new DbField(className, classFieldName, tableName, tableFieldName);
    }
}
