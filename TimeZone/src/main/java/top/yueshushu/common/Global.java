package top.yueshushu.common;

import top.yueshushu.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局参数工具
 */
public class Global {

    public static final ThreadLocal<Map<String, Object>> THREAD_LOCAL_TRANSFER = ThreadLocal.withInitial(HashMap::new);

    public static User getUser() {
        return getTransfer("user");
    }

    public static String getToken() {
        return getTransfer("token");
    }

    public static <T> T getTransfer(String key) {
        return (T) getTransfer().get(key);
    }

    public static void setTransfer(String key, Object value) {
        Map<String, Object> map = getTransfer();
        map.put(key, value);
        THREAD_LOCAL_TRANSFER.set(map);
    }

    public static Map<String, Object> getTransfer() {
        return THREAD_LOCAL_TRANSFER.get();
    }

    public static void release() {
        THREAD_LOCAL_TRANSFER.remove();
    }
}
