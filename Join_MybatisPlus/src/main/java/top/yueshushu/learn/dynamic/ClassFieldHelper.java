package top.yueshushu.learn.dynamic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.pojo.Role;
import top.yueshushu.learn.pojo.User;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author xx
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class ClassFieldHelper extends AbstractClassFieldHelper {
    @PostConstruct
    public void init() {
        // 放置要使用的表信息
        Class[] scanClassList = {User.class, Role.class};
        register(scanClassList);
    }

    /**
     * 将Db class 注册到缓存中
     *
     * @param classes
     */
    private synchronized void register(Class... classes) {
        for (Class aClass : classes) {
            List<DbField> allDbField = doGetOrCreateAllDbField(aClass, null, true, true);
            if (allDbField != null) {
                for (DbField field : allDbField) {
                    String key = SerializableFunctionUtil.getSignatureKey(field.getClassName(), field.getClassFieldName());
                    putIfAbsent(key, field);
                }
            }
        }
    }

    /**
     * 获取class类的所有属性
     * 只能是dbclass，否则，无效
     *
     * @param clazzes db class
     * @return
     */
    public List<DbField> getAllDbField(Set<String> ignoreFieldSet, Class... clazzes) {
        List<DbField> resultList = new ArrayList<>();
        for (Class clazz : clazzes) {
            List<DbField> dbFields = doGetOrCreateAllDbField(clazz, ignoreFieldSet, false, false);
            resultList.addAll(dbFields);
        }

        return resultList;
    }

    public <T> DbField getDbFieldByDbFunction(SerializableFunction<T, ?> function) {
        return doGetDbFieldByFunction(function);
    }

    public <T> List<DbField> getDbFieldByDbFunction(SerializableFunction<T, ?>... function) {
        List<DbField> resultList = new ArrayList<>(function.length);
        for (SerializableFunction<T, ?> serializableFunction : function) {
            resultList.add(doGetDbFieldByFunction(serializableFunction));
        }

        return resultList;
    }

    /**
     * 通过do class和vo function key，获取DbField
     *
     * @param dbClassList do class list
     * @param funcKeyList vo function key
     * @param <T>
     * @return 返回结果
     */
    public <T> List<DbField> getDbFieldByVoFunctionKey(List<Class> dbClassList, List<String> funcKeyList) {
        if (CollUtil.isEmpty(funcKeyList)) {
            return ListUtil.empty();
        }

        List<DbField> resultList = new ArrayList<>(funcKeyList.size());
        for (String funcKey : funcKeyList) {
            DbField dbField = null;
            for (Class aClass : dbClassList) {
                String dbKey = SerializableFunctionUtil.getDbSignatureKeyByVoFuncKey(aClass, funcKey);
                dbField = doGetDbFieldByFieldKeyFromCache(dbKey, true);
                if (dbField != null) {
                    break;
                }
            }
            if (dbField == null) {
                throw new IllegalStateException("getDbFieldByVoFunctionKey exception: " + funcKey);
            }
            resultList.add(dbField);
        }
        return resultList;
    }

    public final List<DbField> getDbFieldByVoFunction(List<Class> dbClassList, List<SerializableFunction> function) {
        List<DbField> resultList = new ArrayList<>(function.size());
        for (SerializableFunction serializableFunction : function) {
            DbField dbField = null;
            for (Class aClass : dbClassList) {
                dbField = doGetDbFieldByVoFunctionFromCache(aClass.getName(), serializableFunction, true);
                if (dbField != null) {
                    break;
                }
            }
            if (dbField == null) {
                throw new IllegalStateException("getDbFieldByVoFunction exception: " + SerializableFunctionUtil.getSignatureKey(serializableFunction));
            }
            resultList.add(dbField);
        }
        return resultList;
    }
}
