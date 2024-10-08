package me.jisung.springplayground.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static me.jisung.springplayground.common.util.ApiResponseUtil.fail;

@RestControllerAdvice
@Slf4j(topic = "ApiExceptionHandler")
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public String apiExceptionHandler(ApiException e) {
        log.info("[ApiExceptionHandler] apiExceptionHandler: {}", e.getMessage());
        return fail(e.getMessage());
    }

}
