package com.zzz.platform.common.swagger;

import com.zzz.platform.common.enumeration.BaseEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SchemaEnum {

    Class<? extends BaseEnum> value();

    String example() default "";

    boolean hidden() default false;

    boolean required() default true;

    String dataType() default "";

    String desc() default "";
}
