package com.zzz.platform.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@AllArgsConstructor
@Getter
public enum SystemEnvironmentEnum implements BaseEnum {

    /**
     * dev
     */
    DEV(SystemEnvironmentNameConst.DEV, "dev environment"),

    /**
     * test
     */
    TEST(SystemEnvironmentNameConst.TEST, "test environment"),


    /**
     * prod
     */
    PROD(SystemEnvironmentNameConst.PROD, "prod environment");

    private final String value;

    private final String desc;

    public static final class SystemEnvironmentNameConst {
        public static final String DEV = "dev";
        public static final String TEST = "test";
        public static final String PROD = "prod";
    }

}
