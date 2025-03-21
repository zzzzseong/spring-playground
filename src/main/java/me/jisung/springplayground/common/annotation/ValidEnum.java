package me.jisung.springplayground.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.jisung.springplayground.common.validator.EnumValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enum 검증을 위한 어노테이션
 * <br><a href="https://gengminy.tistory.com/48#%F-%-F%--%-E%--Custom%--Enum%--Validator%--%EA%B-%AC%ED%--%--%ED%--%--%EA%B-%B-">참고 블로그</a>
 * */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {
    String message() default "올바르지 않은 값입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass();
}
