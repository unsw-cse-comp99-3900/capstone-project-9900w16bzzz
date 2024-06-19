package com.zzz.platform.utils;

import com.zzz.platform.common.enumeration.BaseEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
public class EnumUtil {

    /**
     * Get a description of the instance of the enumerated class that matches the parameter
     *
     * @param value     params
     * @param enumClass Enumeration classes must implement the BaseEnum interface
     * @return String Returns null if no enumeration matches.
     */
    public static String getEnumDescByValue(Object value, Class<? extends BaseEnum> enumClass) {
        if (null == value) {
            return null;
        }
        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.equalsValue(value))
                .findFirst()
                .map(BaseEnum::getDesc)
                .orElse(null);
    }

    public static <T> String getEnumDescByValueList(Collection<T> values, Class<? extends BaseEnum> enumClass) {
        if (CollectionUtils.isEmpty(values)) {
            return "";
        }
        return Stream.of(enumClass.getEnumConstants()).filter(e -> values.contains(e.getValue())).map(BaseEnum::getDesc).collect(Collectors.joining(","));
    }

    /**
     * Get instances of enumerated classes based on parameters
     *
     * @param value     params
     * @param enumClass must impl BaseEnum.java
     * @return BaseEnum Returns null if there is no match
     */
    public static <T extends BaseEnum> T getEnumByValue(Object value, Class<T> enumClass) {
        if (null == value) {
            return null;
        }
        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.equalsValue(value))
                .findFirst()
                .orElse(null);
    }

    public static <T extends BaseEnum> T getEnumByName(String name, Class<T> enumClass) {
        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> StringUtils.equalsIgnoreCase(e.toString(), name))
                .findFirst()
                .orElse(null);
    }
}
