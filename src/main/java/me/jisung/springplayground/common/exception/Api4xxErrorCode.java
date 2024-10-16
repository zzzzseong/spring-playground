package me.jisung.springplayground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Api4xxErrorCode implements ApiErrorCode{

    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "F00000", "SERVICE_UNAVAILABLE"),

    /* validation */
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "F40001", "Invalid request body. Please check your request."),

    /**/
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "F40401", "can not find entity. please check your request"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}