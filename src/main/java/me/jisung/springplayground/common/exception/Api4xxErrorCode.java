package me.jisung.springplayground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Api4xxErrorCode implements ApiErrorCode{

    SERVICE_UNAVAILABLE         (HttpStatus.SERVICE_UNAVAILABLE, "F00000", "SERVICE_UNAVAILABLE"),

    /* 400 - bad request */
    INVALID_REQUEST_PARAMETER       (HttpStatus.BAD_REQUEST, "F40001", "Invalid request parameter. Please check your request."      ),
    INVALID_REQUEST_BODY            (HttpStatus.BAD_REQUEST, "F40002", "Invalid request body. Please check your request."           ),

    /* 403 - unauthorized */
    INVALID_AUTHORIZATION_HEADER    (HttpStatus.UNAUTHORIZED, "F40301", "Invalid authorization header. Please check your request."   ),
    INVALID_JSON_WEB_TOKEN          (HttpStatus.UNAUTHORIZED, "F40302", "Invalid json web token. Please check your request."         ),
    INVALID_USER_CREDENTIALS        (HttpStatus.UNAUTHORIZED, "F40303", "유효하지 않은 사용자 인증 정보."),

    /* 404 - not found */
    REQUEST_BODY_NOT_FOUND          (HttpStatus.NOT_FOUND, "F40401", "request body not found. please check your request"            ),
    ENTITY_NOT_FOUND                (HttpStatus.NOT_FOUND, "F40402", "can not find entity. please check your request"               ),

    /* 409 - conflict */
    ALREADY_EXISTS_ENTITY           (HttpStatus.CONFLICT, "F40901", "이미 DB에 존재하는 데이터")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
