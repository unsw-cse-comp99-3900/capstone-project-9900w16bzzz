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
public enum UserErrorCode implements ErrorCode{


    PARAM_ERROR(30001, "parametric error"),

    DATA_NOT_EXIST(30002, "user not exist"),

    ALREADY_EXIST(30003, "user already exist"),

    REPEAT_SUBMIT(30004, "operate too fast, please wait a second"),

    NO_PERMISSION(30005, "permission denied"),

    DEVELOPING(30006, "The system is under development..."),

    LOGIN_STATE_INVALID(30007, "You are not logged in or your login is not working, please log in again!"),

    USER_STATUS_ERROR(30008, "User status anomaly"),

    FORM_REPEAT_SUBMIT(30009, "Please do not duplicate submissions"),

    LOGIN_FAIL_LOCK(30010, "Login Continuous Failure has been locked out from logging in"),
    LOGIN_FAIL_WILL_LOCK(30011, "Alerts will be locked for consecutive failed login attempts"),

    LOGIN_ACTIVE_TIMEOUT(30012, "Long time without operating system, need to log in again");

    private final int code;

    private final String msg;

    private final String level;

    UserErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.level = LEVEL_USER;
    }
}
