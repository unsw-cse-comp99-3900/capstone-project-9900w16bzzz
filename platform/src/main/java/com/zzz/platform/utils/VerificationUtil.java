package com.zzz.platform.utils;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
public class VerificationUtil {

    /**
     * pwd regexp
     */
    public static final String PWD_REGEXP = "^[A-Za-z0-9.]{6,15}$";

    /**
     * email
     */
    public static final String EMAIL = "^(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$\n";

}
