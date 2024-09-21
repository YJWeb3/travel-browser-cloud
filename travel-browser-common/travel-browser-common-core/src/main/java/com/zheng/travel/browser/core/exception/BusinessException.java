package com.zheng.travel.browser.core.exception;

import com.zheng.travel.browser.core.utils.R;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private Integer code = R.CODE_ERROR;

    public BusinessException() {
        super(R.MSG_ERROR);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
