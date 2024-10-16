package me.jisung.springplayground.common.exception;

import org.springframework.http.HttpStatus;

public interface ApiErrorCode {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
