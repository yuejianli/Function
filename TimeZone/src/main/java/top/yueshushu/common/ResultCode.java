package top.yueshushu.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-10-31
 */

public class ResultCode {
    public static final ResultCode SUCCESS = new ResultCode(true, 10000, "success");
    public static final ResultCode FAIL = new ResultCode(false, 11111, "failure");
    public static final ResultCode SERVER_ERROR = new ResultCode(false, 10001, "Server busy, please try again later!");
    public static final ResultCode UN_AUTH = new ResultCode(false, 10002, "Not logged in or not authorized");
    public static final ResultCode DB_ERROR = new ResultCode(false, 10003, "The database operation failed. Please contact the administrator");
    public static final ResultCode INVALID_PARAM = new ResultCode(false, 10009, "param invalid");
    public static final ResultCode CURRENT_INTERFACE_BUSY = new ResultCode(false, 10047, "Current interface busy, please try again later!");
    private static final ResultCode[] VALUES = getStaticFieldValues(ResultCode.class);
    private boolean success;
    private int code;
    private String message;

    protected ResultCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean equalsByCode(int code) {
        return this.code == code;
    }

    public static ResultCode getByCode(int code) {
        ResultCode[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ResultCode typeEnum = var1[var3];
            if (typeEnum.code == code) {
                return typeEnum;
            }
        }

        return null;
    }

    public static ResultCode[] values() {
        return VALUES;
    }

    protected static <T extends ResultCode> ResultCode[] getStaticFieldValues(Class<T> clazz) {
        List<T> resultCodes = new ArrayList();
        Field[] fields = clazz.getFields();

        try {
            Field[] var3 = fields;
            int var4 = fields.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getType().isAssignableFrom(clazz)) {
                    resultCodes.add((T) field.get(clazz));
                }
            }
        } catch (IllegalAccessException var7) {
        }

        return (ResultCode[]) resultCodes.toArray(new ResultCode[0]);
    }

    public boolean isSuccess() {
        return this.success;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "ResultCode(success=" + this.isSuccess() + ", code=" + this.getCode() + ", message=" + this.getMessage() + ")";
    }
}
