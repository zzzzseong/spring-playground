package me.jisung.springplayground.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * API 요청 검증 관련 상수 클래스
 * <br><a href="https://regexr.com/">정규표현식 테스트 사이트</a>
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationConst {

    public static final String MESSAGE_NOT_NULL = "필수 입력값이 누락되었습니다.";
    public static final String MESSAGE_REGEXP_INVALID = "입력값이 유효하지 않습니다.";

    public static final String REGEXP_EMAIL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    public static final String REGEXP_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
}
