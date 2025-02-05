package me.jisung.springplayground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Api4xxErrorCode implements ApiErrorCode{

    /* 400 - Bad Request - 잘못된 요청*/
    INVALID_REQUEST_PARAMETER       (HttpStatus.BAD_REQUEST, "F40001", "유효하지않은 파라미터, 필드값"),
    INVALID_REQUEST_BODY            (HttpStatus.BAD_REQUEST, "F40002", "유효하지않은 요청 본문"),
    FILE_EXTENSION_NOT_FOUND        (HttpStatus.BAD_REQUEST, "F40021", "파일 확장자를 찾을 수 없음"),
    FILE_EXTENSION_NOT_ALLOWED      (HttpStatus.BAD_REQUEST, "F40022", "허용되지 않는 파일 확장자"),

    /* 401 - Unauthorized - 접근 권한 없음 */
    INVALID_AUTHORIZATION_HEADER    (HttpStatus.UNAUTHORIZED, "F40101", "인증 해더(Authorization)가 없거나 유효하지 않음"),
    INVALID_JSON_WEB_TOKEN          (HttpStatus.UNAUTHORIZED, "F40102", "유효하지 않은 JWT - 만료, 손상 등"),
    INVALID_USER_CREDENTIALS        (HttpStatus.UNAUTHORIZED, "F40103", "인가 요청 정보(ID, PW)와 일치하는 사용자 정보가 없음"),
    NOT_FOUND_AUTHENTICATION        (HttpStatus.UNAUTHORIZED, "F40104", "Security Context 인증 정보를 찾을 수 없음"),

    /* 403 - Forbidden - 접근 금지 */
    /* 401 인증 처리 이외의 사유로 리소스에 대한 액세스가 금지되었음을 의미 */
    /* 리소스의 존재 자체를 은폐하고 싶은 경우는 404 응답 코드를 사용 */
    NOT_ALLOWED_IP_ADDRESS      (HttpStatus.FORBIDDEN, "F40301", "접근이 허용되지 않은 IP"),

    /* 404 - Not Found - 리소스를 찾을 수 없음 */
    REQUEST_BODY_NOT_FOUND          (HttpStatus.NOT_FOUND, "F40401", "사용자의 요청에서 요청 본문을 찾을 수 없음 - 400으로 이동 필요"),
    ENTITY_NOT_FOUND                (HttpStatus.NOT_FOUND, "F40402", "데이터베이스에서 엔티티를 찾을 수 없음"),

    /* 409 - Conflict - 서버에 이미 존재하는 리소스와 충돌 */
    ALREADY_EXISTS_ENTITY           (HttpStatus.CONFLICT, "F40901", "이미 DB에 존재하는 엔티티"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
