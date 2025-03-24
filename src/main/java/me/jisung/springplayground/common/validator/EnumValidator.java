package me.jisung.springplayground.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.annotation.ValidEnum;

@Slf4j
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private ValidEnum validEnum;

    @Override
    public void initialize(ValidEnum validEnum) {
        this.validEnum = validEnum;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        Object[] enumValues = validEnum.enumClass().getEnumConstants();

        for (Object enumValue : enumValues) {
            if (value.equalsIgnoreCase(String.valueOf(enumValue))) return true;
        }

        return false;
    }
}
