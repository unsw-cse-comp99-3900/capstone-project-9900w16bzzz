package com.zzz.platform.common.code;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
public interface ErrorCode {

    /**
     * system
     */
    String LEVEL_SYSTEM = "system";

    /**
     * user level
     */
    String LEVEL_USER = "user";

    /**
     * unexpected
     */
    String LEVEL_UNEXPECTED = "unexpected";

    /**
     * error cord
     */
    int getCode();

    /**
     * error msg
     *
     */
    String getMsg();

    /**
     * error level
     */
    String getLevel();

}
