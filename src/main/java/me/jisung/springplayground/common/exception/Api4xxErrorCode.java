package me.jisung.springplayground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Api4xxErrorCode implements ApiErrorCode{

    /* 400 - bad request */
    INVALID_REQUEST_PARAMETER       (HttpStatus.BAD_REQUEST, "F40001", "유효하지않은 파라미터, 필드값"),
    INVALID_REQUEST_BODY            (HttpStatus.BAD_REQUEST, "F40002", "유효하지않은 요청 본문"),

    /* 401 - unauthorized */
    INVALID_AUTHORIZATION_HEADER    (HttpStatus.UNAUTHORIZED, "F40101", "인증 해더(Authorization)가 없거나 유효하지 않음"),
    INVALID_JSON_WEB_TOKEN          (HttpStatus.UNAUTHORIZED, "F40102", "유효하지 않은 JWT - 만료, 손상 등"),
    INVALID_USER_CREDENTIALS        (HttpStatus.UNAUTHORIZED, "F40103", "인가 요청 정보(ID, PW)와 일치하는 사용자 정보가 없음"),
    NOT_FOUND_AUTHENTICATION        (HttpStatus.UNAUTHORIZED, "F40104", "Security Context 인증 정보를 찾을 수 없음"),

    /* 404 - not found */
    REQUEST_BODY_NOT_FOUND          (HttpStatus.NOT_FOUND, "F40401", "사용자의 요청에서 요청 본문을 찾을 수 없음 - 400으로 이동 필요"),
    ENTITY_NOT_FOUND                (HttpStatus.NOT_FOUND, "F40402", "데이터베이스에서 엔티티를 찾을 수 없음"),

    /* 409 - conflict */
    ALREADY_EXISTS_ENTITY           (HttpStatus.CONFLICT, "F40901", "이미 DB에 존재하는 엔티티"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
