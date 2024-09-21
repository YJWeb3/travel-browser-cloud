package com.zheng.travel.browser.core.utils;

import com.zheng.travel.browser.core.exception.BusinessException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @param <T>
 * @author zhengge
 */
@Setter
@Getter
@NoArgsConstructor
public class R<T> {

    public static final int CODE_SUCCESS = 200;
    public static final String MSG_SUCCESS = "操作成功";
    public static final int CODE_NOLOGIN = 401;
    public static final String MSG_NOLOGIN = "请先登录";
    public static final int CODE_ERROR = 500;
    public static final int CODE_REGISTER_ERROR = 500100;
    public static final int CODE_SMS_ERROR = 500101;
    public static final String MSG_ERROR = "系统异常，请联系管理员";
    public static final int CODE_ERROR_PARAM = 501;

    /**
     * 区分不同结果, 而不再是true或者false
     */
    private int code;
    private String msg;
    /**
     * 除了操作结果之后, 还行携带数据返回
     */
    private T data;

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok(T data) {
        return new R<>(CODE_SUCCESS, MSG_SUCCESS, data);
    }

    public static <T> R<T> ok() {
        return new R<>(CODE_SUCCESS, MSG_SUCCESS, null);
    }

    public static <T> R<T> error(int code, String msg, T data) {
        return new R<>(code, msg, data);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public static <T> R<T> defaultError() {
        return new R<>(CODE_ERROR, MSG_ERROR, null);
    }


    public static <T> R<T> noLogin() {
        return new R<>(CODE_NOLOGIN, MSG_NOLOGIN, null);
    }

    public static <T> R<T> noPermission() {
        return new R<>(403, "非法访问", null);
    }

    public T checkAndGet() {
        if (code != CODE_SUCCESS) {
            throw new BusinessException(code, msg);
        }
        return data;
    }
}
