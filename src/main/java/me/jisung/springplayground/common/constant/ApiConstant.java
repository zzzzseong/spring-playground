package me.jisung.springplayground.common.constant;

public class ApiConstant {

    private ApiConstant() { throw new IllegalStateException("utility class cannot be instantiated"); }

    public static final String RESPONSE_CODE_SUCCESS = "S20000";
    public static final String BEARER_PREFIX = "Bearer ";

    /* validation */
    public static final String NOT_NULL_MESSAGE = "필수 입력값이 누락되었습니다.";
}
