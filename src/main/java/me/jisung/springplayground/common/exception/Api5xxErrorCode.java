package me.jisung.springplayground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Api5xxErrorCode implements ApiErrorCode {

    UNHANDLED_EXCEPTION     (HttpStatus.INTERNAL_SERVER_ERROR, "F50000", "예외처리되지 않은 런타임 에러"),

    /* AES */
    AES_ENCRYPTION_FAILED   (HttpStatus.INTERNAL_SERVER_ERROR, "F50001", "AES 암호화 실패"),
    AES_DECRYPTION_FAILED   (HttpStatus.INTERNAL_SERVER_ERROR, "F50002", "AES 복호화 실패"),

    /* ??? */
    SERVICE_UNAVAILABLE     (HttpStatus.SERVICE_UNAVAILABLE, "F00301", "SERVICE_UNAVAILABLE"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
