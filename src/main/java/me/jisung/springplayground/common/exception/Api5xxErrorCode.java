package me.jisung.springplayground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Api5xxErrorCode implements ApiErrorCode {

    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "F50000", "unhandled exception occured, please contact the administrator."),

    /* AES */
    AES_ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F50001", "AES encryption failed."),
    AES_DECRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F50002", "AES decryption failed."),

    /* format */
    INVALID_JSON_FORMAT_5XX(HttpStatus.INTERNAL_SERVER_ERROR, "F50013", "INVALID_JSON_FORMAT_5XX"),
    INVALID_URI_FORMAT_5XX(HttpStatus.INTERNAL_SERVER_ERROR, "F50014", "INVALID_URI_FORMAT_5XX"),

    /* http */
    HTTP_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F50006", "HTTP_REQUEST_FAILED"),

    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "F00301", "SERVICE_UNAVAILABLE"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}