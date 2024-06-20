package com.zzz.platform.common.validator.enumeration;

import com.zzz.platform.common.enumeration.BaseEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
public class EnumValidator implements ConstraintValidator<CheckEnum, Object> {

    /**
     * Collection of instances of enumerated classes
     */
    private List<Object> enumValList;

    /**
     * is necessary
     */
    private boolean required;

    @Override
    public void initialize(CheckEnum constraintAnnotation) {
        // Get the object of the enumerated class passed in by the annotation
        required = constraintAnnotation.required();
        Class<? extends BaseEnum> enumClass = constraintAnnotation.value();
        enumValList = Stream.of(enumClass.getEnumConstants()).map(BaseEnum::getValue).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (null == value) {
            return !required;
        }

        if (value instanceof List) {
            // if it is list
            return this.checkList((List<Object>) value);
        }

        // is it illegal
        return enumValList.contains(value);
    }

    /**
     * Checksum type
     *
     */
    private boolean checkList(List<Object> list) {
        if (required && list.isEmpty()) {
            // not empty
            return false;
        }
        // no repeat
        long count = list.stream().distinct().count();
        if (count != list.size()) {
            return false;
        }
        return enumValList.containsAll(list);
    }
}
