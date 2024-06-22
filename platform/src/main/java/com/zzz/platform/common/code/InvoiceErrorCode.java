package com.zzz.platform.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/22
 */
@Getter
@AllArgsConstructor
public enum InvoiceErrorCode implements ErrorCode{

    GET_API_TOKEN_FAILED(40001, "Getting API token from url failed, please try it later"),

    CONTENT_MD5_NOT_EQUAL(40002, "The md5 of content is not equal, please check it"),

    API_VALIDATION_REQUEST_FAILED(40003, "API for validation request failed, please check http status"),
    ;

    private final int code;

    private final String msg;

    private final String level;

    InvoiceErrorCode(int code,String msg) {
        this.msg = msg;
        this.code = code;
        this.level = LEVEL_USER;
    }
}
