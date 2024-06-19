package com.zzz.platform.common.exception;

import com.zzz.platform.common.code.ErrorCode;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
