package com.zzz.platform.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Getter
@AllArgsConstructor
public enum SystemErrorCode implements ErrorCode {
    SYSTEM_ERROR(10001, "System error"),

    ;

    private final int code;

    private final String msg;

    private final String level;

    SystemErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_SYSTEM;
    }
}
