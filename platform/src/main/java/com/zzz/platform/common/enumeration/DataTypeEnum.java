package com.zzz.platform.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Getter
@AllArgsConstructor
public enum DataTypeEnum implements BaseEnum{
    /**
     * normal data
     */
    NORMAL(1, "normal data"),

    /**
     * encrypted data
     */
    ENCRYPT(10, "encrypted data"),
    ;
    private final Integer value;

    private final String desc;
}
