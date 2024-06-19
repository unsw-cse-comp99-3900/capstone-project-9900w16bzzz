package com.zzz.platform.common.validator.enumeration;

import com.zzz.platform.common.enumeration.BaseEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface CheckEnum {

    /**
     * Default error message
     *
     * @return String
     */
    String message();

    /**
     * Enumerated class objects must implement the BaseEnum interface.
     *
     */
    Class<? extends BaseEnum> value();

    /**
     * compulsory
     *
     * @return boolean
     */
    boolean required() default false;

    //The following two attributes must be added : otherwise an error will be reported.
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
