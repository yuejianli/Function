package top.yueshushu.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zk on 2018/12/17.
 */
@Data
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = -4149524177946388459L;

    //操作是否成功,true为成功，false操作失败
    private boolean success;
    //操作代码
    private int code;
    //提示信息
    private String message;
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(ResultCode resultCode) {
        this(resultCode, null);
    }

    public ResponseResult(ResultCode resultCode, T data) {
        this(resultCode.isSuccess(), resultCode.getCode(), resultCode.getMessage(), data);
    }

    private ResponseResult(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseResult<T> buildFail() {
        return buildFail(ResultCode.FAIL);
    }

    public static <T> ResponseResult<T> buildFail(String message) {
        return buildFail(ResultCode.FAIL.getCode(), message);
    }

    public static <T> ResponseResult<T> buildFail(ResultCode resultCode) {
        return buildFail(resultCode.getCode(), resultCode.getMessage());
    }

    public static <T> ResponseResult<T> buildFail(int code, String message) {
        return build(false, code, message, null);
    }

    public static <T> ResponseResult<T> buildFail(ResultCode resultCode, T data) {
        return buildFail(resultCode, null, data);
    }

    public static <T> ResponseResult<T> buildFail(ResultCode resultCode, String message) {
        return buildFail(resultCode, message, null);
    }

    public static <T> ResponseResult<T> buildFail(ResultCode resultCode, String message, T data) {
        return buildFail(resultCode.getCode(), message, data);
    }

    public static <T> ResponseResult<T> buildFail(int code, String message, T data) {
        return build(false, code, message, data);
    }

    public static <T> ResponseResult<T> buildSucc() {
        return buildSucc(ResultCode.SUCCESS);
    }

    public static <T> ResponseResult<T> buildSucc(T data) {
        return buildSucc(ResultCode.SUCCESS, data);
    }

    public static <T> ResponseResult<T> buildSucc(ResultCode resultCode) {
        return buildSucc(resultCode.getCode(), resultCode.getMessage());
    }

    public static <T> ResponseResult<T> buildSucc(int code, String message) {
        return buildSucc(code, message, null);
    }

    public static <T> ResponseResult<T> buildSucc(ResultCode resultCode, T data) {
        return buildSucc(resultCode, null, data);
    }

    public static <T> ResponseResult<T> buildSucc(ResultCode resultCode, String message, T data) {
        return buildSucc(resultCode.getCode(), message, data);
    }

    public static <T> ResponseResult<T> buildSucc(int code, String message, T data) {
        return build(true, code, message, data);
    }

    private static <T> ResponseResult<T> build(boolean success, int code, String message, T data) {
        return new ResponseResult<>(success, code, message, data);
    }

    @Override
    public String toString() {
        return "{success: " + this.success + ", code: " + this.code + ", message: " + this.message + ", data: " + this.data + "}";
    }
}
