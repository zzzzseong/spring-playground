package me.jisung.springplayground.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    /**
     * ApiException Constructor
     * @param exception: 스택 정보와 메시지, 컨텍스트 정보 등을 전달하기 위해 기존에 발생했던 예외를 전달받아 상위 클래스에 전달한다.
     * @param errorCode: ApiException 처리를 위해 미리 정의해둔 enum 코드
     * */
    public ApiException(Throwable exception, ApiErrorCode errorCode) {
        super(exception);
        this.httpStatus = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }
    public ApiException(Exception exception, ApiErrorCode errorCode) {
        super(exception);
        this.httpStatus = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }
    public ApiException(ApiErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }
    public ApiException(HttpStatus status, String message) {
        super(message);
        this.httpStatus = status;
        this.message = message;
    }
}
