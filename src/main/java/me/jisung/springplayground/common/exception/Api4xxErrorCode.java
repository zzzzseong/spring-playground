package me.jisung.springplayground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 4xx 클라이언트 오류 코드에 대한 정의를 나타내는 Enum 클래스입니다.
 * 이 클래스는 클라이언트 측에서 발생한 요청 오류에 대한 코드와 메시지를 정의합니다.
 * 각 오류 코드에는 적절한 HTTP 상태 코드와 고유의 에러 코드, 설명이 포함됩니다.
 *
 * <p>4xx 상태 코드 목록:</p>
 * <ul>
 *   <li><b>400 - Bad Request:</b> 클라이언트의 요청 구문이 잘못되었습니다.</li>
 *   <li><b>401 - Unauthorized:</b> 클라이언트는 리소스에 액세스할 권한이 없습니다.</li>
 *   <li><b>402 - Payment Required:</b> 리소스를 액세스하기 위해 결제가 필요하지만 실제로는 사용되지 않습니다.</li>
 *   <li><b>403 - Forbidden:</b> 리소스에 대한 액세스가 금지되었습니다.</li>
 *   <li><b>404 - Not Found:</b> 지정한 리소스를 찾을 수 없습니다.</li>
 *   <li><b>405 - Method Not Allowed:</b> 요청한 메소드가 지원되지 않습니다.</li>
 *   <li><b>406 - Not Acceptable:</b> 클라이언트가 요청한 내용 타입을 서버가 처리할 수 없습니다.</li>
 *   <li><b>407 - Proxy Authentication Required:</b> 프록시 서버 인증이 필요합니다.</li>
 *   <li><b>408 - Request Timeout:</b> 서버가 클라이언트의 요청을 기다리다 타임아웃이 발생했습니다.</li>
 *   <li><b>409 - Conflict:</b> 서버가 요청을 처리하는 동안 충돌이 발생했습니다.</li>
 *   <li><b>410 - Gone:</b> 리소스가 현재는 존재하지 않습니다.</li>
 *   <li><b>411 - Length Required:</b> 요청 헤더에 Content-Length가 지정되어야 합니다.</li>
 *   <li><b>412 - Precondition Failed:</b> 사전 조건이 서버와 맞지 않습니다.</li>
 *   <li><b>413 - Request Entity Too Large:</b> 요청 메시지가 너무 큽니다.</li>
 *   <li><b>414 - Request-URI Too Large:</b> 요청 URI가 너무 길어서 서버에서 처리할 수 없습니다.</li>
 *   <li><b>415 - Unsupported Media Type:</b> 클라이언트가 요청한 미디어 타입을 서버가 지원하지 않습니다.</li>
 *   <li><b>416 - Range Not Satisfiable:</b> 지정된 리소스의 범위가 서버 리소스와 맞지 않습니다.</li>
 *   <li><b>417 - Expectation Failed:</b> 클라이언트가 지정한 Expect 헤더를 서버가 이해할 수 없습니다.</li>
 *   <li><b>418 ~ 421 - Unassigned:</b> 현재 할당되지 않은 상태 코드입니다.</li>
 *   <li><b>422 - Unprocessable Entity:</b> 클라이언트가 송신한 XML은 구문은 맞지만 의미상 오류가 있습니다.</li>
 *   <li><b>423 - Locked:</b> 지정한 리소스가 잠겨 있습니다.</li>
 *   <li><b>424 - Failed Dependency:</b> 다른 작업의 실패로 인해 본 요청도 실패했습니다.</li>
 *   <li><b>426 - Upgrade Required:</b> 클라이언트가 사용할 프로토콜을 업그레이드해야 합니다.</li>
 *   <li><b>428 - Precondition Required:</b> If-Match와 같은 사전 조건을 지정하는 헤더가 필요합니다.</li>
 *   <li><b>429 - Too Many Requests:</b> 클라이언트가 너무 많은 요청을 보내서 서버에서 거부합니다.</li>
 *   <li><b>431 - Request Header Fields Too Large:</b> 요청 헤더의 크기가 너무 큽니다.</li>
 *   <li><b>444 - Connection Closed Without Response:</b> 응답 없이 연결을 종료한 경우(NGINX).</li>
 *   <li><b>451 - Unavailable For Legal Reasons:</b> 법적 사유로 요청한 리소스를 사용할 수 없습니다.</li>
 *   <li><b>452 ~ 499 - Unassigned:</b> 현재 할당되지 않은 상태 코드입니다.</li>
 * </ul>
 */
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
