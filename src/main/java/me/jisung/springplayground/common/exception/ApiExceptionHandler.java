package me.jisung.springplayground.common.exception;

import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.entity.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Objects;

@RestControllerAdvice
@Slf4j(topic = "ApiExceptionHandler")
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ApiResponse<ApiErrorVo> apiExceptionHandler(ApiException e) {
        log.error("[API RESPONSE FAILED] ApiException - code: {}, message: {}", e.getCode(), e.getMessage());

        Throwable cause = e.getCause();
        if(!Objects.isNull(cause)) log.error("caused by: ", cause);

        return ApiResponse.fail(e.getCode(), new ApiErrorVo(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<ApiErrorVo> unhandledExceptionHandler(Exception e) {
        Api5xxErrorCode errorCode = Api5xxErrorCode.UNHANDLED_EXCEPTION;
        ApiException apiException = ApiException.builder()
            .e(e)
            .httpStatus(errorCode.getHttpStatus())
            .code(errorCode.getCode())
            .message(e.getMessage())
            .build();
        return this.apiExceptionHandler(apiException);
    }

    /**
     * 외부 http 통신 중 발생한 예외를 처리하는 핸들러
     * @see me.jisung.springplayground.common.component.HttpHelper
     * */
    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ApiResponse<ApiErrorVo> handleHttpClientErrorException(HttpStatusCodeException e) {
        ApiException apiException = ApiException.builder()
                .e(e)
                .httpStatus(HttpStatus.valueOf(e.getStatusCode().value()))
                .code(Api5xxErrorCode.HTTP_REQUEST_FAILED.getCode())
                .message(e.getResponseBodyAsString())
                .build();
        return apiExceptionHandler(apiException);
    }

    /**
     * RequestParam validation exception handler
     * */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<ApiErrorVo> validationExceptionHandler(MissingServletRequestParameterException e) {
        Api4xxErrorCode errorCode = Api4xxErrorCode.INVALID_REQUEST_PARAMETER;
        ApiException apiException = ApiException.builder()
            .e(e)
            .httpStatus(errorCode.getHttpStatus())
            .code(errorCode.getCode())
            .message(e.getMessage())
            .build();

        return this.apiExceptionHandler(apiException);
    }

    /**
     * 요청 본문 필드 검증 실패 시 발생하는 예외 처리 (jakarta.validation.Valid 사용 시)
     * <br>- application/json 요청에 대해 validation 통과하지 못했을 시 발생
     * <br>- multipart/form-data 요청에 대해 validation 통과하지 못했을 시 발생
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<ApiErrorVo> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiException apiException = new ApiException(e, Api4xxErrorCode.INVALID_REQUEST_BODY, message);
        return this.apiExceptionHandler(apiException);
    }
}
