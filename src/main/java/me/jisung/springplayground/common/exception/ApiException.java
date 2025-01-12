package me.jisung.springplayground.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Builder
    public ApiException(Throwable e, HttpStatus httpStatus, String code, String message) {
        super(e);
        this.httpStatus = httpStatus;
        this.code       = code;
        this.message    = message;
    }
    public ApiException(Throwable e, ApiErrorCode errorCode) {
        super(e);
        this.httpStatus = errorCode.getHttpStatus();
        this.code       = errorCode.getCode();
        this.message    = errorCode.getMessage();
    }
    public ApiException(Throwable e, ApiErrorCode errorCode, String message) {
        super(e);
        this.httpStatus = errorCode.getHttpStatus();
        this.code       = errorCode.getCode();
        this.message    = message;
    }

    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.httpStatus = status;
        this.code       = code;
        this.message    = message;
    }
    public ApiException(ApiErrorCode errorCode) {
        this.httpStatus = errorCode.getHttpStatus();
        this.code       = errorCode.getCode();
        this.message    = errorCode.getMessage();
    }
    public ApiException(ApiErrorCode errorCode, String message) {
        this.httpStatus = errorCode.getHttpStatus();
        this.code       = errorCode.getCode();
        this.message    = message;
    }
}
