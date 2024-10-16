package me.jisung.springplayground.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.util.Objects;

import static me.jisung.springplayground.common.util.ApiResponseUtil.fail;

@RestControllerAdvice
@Slf4j(topic = "ApiExceptionHandler")
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public String apiExceptionHandler(ApiException e) {
        log.error("[API RESPONSE FAIL] ApiException - code: {}, message: {}", e.getCode(), e.getMessage());

        Throwable cause = e.getCause();
        if(!Objects.isNull(cause)) log.error("caused by: ", cause);

        return fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public String validationExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiException apiException = new ApiException(Api4xxErrorCode.INVALID_REQUEST_BODY, message);
        return apiExceptionHandler(apiException);
    }

    @ExceptionHandler(Exception.class)
    public String unhandledExceptionHandler(Exception e) {
        return apiExceptionHandler(new ApiException(e, Api5xxErrorCode.UNHANDLED_EXCEPTION));
    }
}
