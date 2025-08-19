package me.jisung.springplayground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 5xx 서버 오류 코드에 대한 정의를 나타내는 Enum 클래스입니다.
 * 이 클래스는 서버 측에서 발생한 에러에 대한 코드와 메시지를 정의합니다.
 * 각 오류 코드에는 적절한 HTTP 상태 코드와 고유의 에러 코드, 설명이 포함됩니다.
 *
 * <p>5xx 상태 코드 목록:</p>
 * <ul>
 *   <li><b>500 - Internal Server Error:</b> 서버에 에러가 발생하였으며, 클라이언트는 이를 알 수 없음.</li>
 *   <li><b>501 - Not Implemented:</b> 요청한 URI의 메소드에 대해 서버가 구현하고 있지 않음.</li>
 *   <li><b>502 - Bad Gateway:</b> 서버가 다른 서버로부터 잘못된 응답을 받음.</li>
 *   <li><b>503 - Service Unavailable:</b> 서버가 일시적으로 서비스를 제공할 수 없음.</li>
 *   <li><b>504 - Gateway Timeout:</b> 서버가 다른 서버로부터 응답을 기다리다 타임아웃 발생.</li>
 *   <li><b>505 - HTTP Version Not Supported:</b> 서버가 클라이언트가 요청한 HTTP 버전을 지원하지 않음.</li>
 *   <li><b>506 - Unassigned:</b> 현재 할당되지 않은 상태 코드입니다.</li>
 *   <li><b>507 - Insufficient Storage:</b> 서버에 저장 공간 부족으로 처리에 실패한 경우.</li>
 *   <li><b>512 ~ 599 - Unassigned:</b> 현재 할당되지 않은 상태 코드입니다.</li>
 * </ul>
 */
@Getter
@AllArgsConstructor
public enum Api5xxErrorCode implements ApiErrorCode {

    // 500 - Internal Server Error
    UNHANDLED_EXCEPTION     (HttpStatus.INTERNAL_SERVER_ERROR, "F50000", "예외처리되지 않은 런타임 에러"),
    AES_ENCRYPTION_FAILED   (HttpStatus.INTERNAL_SERVER_ERROR, "F50001", "AES 암호화 실패"),
    AES_DECRYPTION_FAILED   (HttpStatus.INTERNAL_SERVER_ERROR, "F50002", "AES 복호화 실패"),
    SHA_ENCRYPTION_FAILED   (HttpStatus.INTERNAL_SERVER_ERROR, "F50003", "SHA 암호화 실패"),

    // 502 - Bad Gateway
    HTTP_REQUEST_FAILED     (HttpStatus.BAD_GATEWAY, "F50201", "HTTP 요청에 실패. 외부 서버로부터 에러를 응답 받음."),

    // 503 - Service Unavailable
    SERVICE_UNAVAILABLE     (HttpStatus.SERVICE_UNAVAILABLE,   "F50004", "SERVICE_UNAVAILABLE")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
