package com.zzz.platform.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/2
 */
@AllArgsConstructor
@Getter
public enum FileType implements BaseEnum{
    JSON(1,"json"),
    XML(2,"xml"),
    PDF(3,"pdf");
    ;
    final Integer value;
    // column name in database
    final String desc;
}
