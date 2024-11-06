package me.jisung.springplayground.common.exception;

import java.net.BindException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.entity.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "ApiExceptionHandler")
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ApiResponse<String> apiExceptionHandler(ApiException e) {
        log.error("[API RESPONSE FAILED] ApiException - code: {}, message: {}", e.getCode(), e.getMessage());

        Throwable cause = e.getCause();
        if(!Objects.isNull(cause)) log.error("caused by: ", cause);

        return ApiResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> unhandledExceptionHandler(Exception e) {
        Api5xxErrorCode errorCode = Api5xxErrorCode.UNHANDLED_EXCEPTION;
        ApiException apiException = ApiException.builder()
            .e(e)
            .httpStatus(errorCode.getHttpStatus())
            .code(errorCode.getCode())
            .message(e.getMessage())
            .build();
        return apiExceptionHandler(apiException);
    }

    /**
     * RequestParam validation exception handler
     * */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<String> validationExceptionHandler(MissingServletRequestParameterException e) {
        Api4xxErrorCode errorCode = Api4xxErrorCode.INVALID_REQUEST_PARAMETER;
        ApiException apiException = ApiException.builder()
            .e(e)
            .httpStatus(errorCode.getHttpStatus())
            .code(errorCode.getCode())
            .message(e.getMessage())
            .build();

        return apiExceptionHandler(apiException);
    }

    /**
     * RequestBody, ModelAttribute validation exception handler
     * */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ApiResponse<String> validationExceptionHandler(MethodArgumentNotValidException e) {
        Api4xxErrorCode errorCode = Api4xxErrorCode.INVALID_REQUEST_BODY;
        ApiException apiException = ApiException.builder()
            .e(e)
            .httpStatus(errorCode.getHttpStatus())
            .code(errorCode.getCode())
            .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
            .build();

        return apiExceptionHandler(apiException);
    }
}
