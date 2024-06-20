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
    public static final String EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

}
