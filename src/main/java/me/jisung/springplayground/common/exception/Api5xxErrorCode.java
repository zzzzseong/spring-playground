package me.jisung.springplayground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Api5xxErrorCode implements ApiErrorCode {

    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "F50000", "unhandled exception occured, please contact the administrator."),

    /* encrypt */
    ENCRYPTION_NO_SUCH_PADDING(HttpStatus.INTERNAL_SERVER_ERROR, "F50001", "No such padding exception during encryption"),
    ENCRYPTION_NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "F50002", "No such algorithm exception during encryption"),
    ENCRYPTION_INVALID_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "F50003", "Invalid key exception during encryption"),
    ENCRYPTION_ILLEGAL_BLOCK_SIZE(HttpStatus.INTERNAL_SERVER_ERROR, "F50004", "Illegal block size exception during encryption"),
    ENCRYPTION_BAD_PADDING(HttpStatus.INTERNAL_SERVER_ERROR, "F50005", "Bad padding exception during encryption"),
    ENCRYPTION_INVALID_ALGORITHM_PARAMETER(HttpStatus.INTERNAL_SERVER_ERROR, "F50006", "Invalid algorithm parameter exception during encryption"),

    /* decrypt */
    DECRYPTION_NO_SUCH_PADDING(HttpStatus.INTERNAL_SERVER_ERROR, "F50007", "No such padding exception during decryption"),
    DECRYPTION_NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "F50008", "No such algorithm exception during decryption"),
    DECRYPTION_INVALID_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "F50009", "Invalid key exception during decryption"),
    DECRYPTION_ILLEGAL_BLOCK_SIZE(HttpStatus.INTERNAL_SERVER_ERROR, "F50010", "Illegal block size exception during decryption"),
    DECRYPTION_BAD_PADDING(HttpStatus.INTERNAL_SERVER_ERROR, "F50011", "Bad padding exception during decryption"),
    DECRYPTION_INVALID_ALGORITHM_PARAMETER(HttpStatus.INTERNAL_SERVER_ERROR, "F50012", "Invalid algorithm parameter exception during decryption"),

    /* format */
    INVALID_JSON_FORMAT_5XX(HttpStatus.INTERNAL_SERVER_ERROR, "F50013", "INVALID_JSON_FORMAT_5XX"),
    INVALID_URI_FORMAT_5XX(HttpStatus.INTERNAL_SERVER_ERROR, "F50014", "INVALID_URI_FORMAT_5XX"),

    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "F00301", "SERVICE_UNAVAILABLE"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}