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

    UPBRAINSAI_API_REQUEST_FAILED(40004, "API for upbrainsai request failed, please check it"),

    JSON_INVOICE_FORMAT_ERROR(40005, "Upload json invoice file format error"),

    INVOICE_FILE_FORMAT_ERROR(40006, "Upload invoice file format error, only accept pdf/json invoice file"),

    INVOICE_FILE_DOES_NOT_EXIST(40007, "Selected invoice file not exist, please check it"),

    INVOICE_LIST_QUERY_FAILED(40008, "Query invoice list failed, please check it"),

    VALIDATION_RULES_ERROR(40009, "Validation rules error, please check it"),
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
