package me.jisung.springplayground.common.constant;

public class ApiConstant {

    private ApiConstant() {}

    public static final String RESPONSE_RESULT_SUCCESS = "success";
    public static final String RESPONSE_RESULT_FAIL = "fail";

    public static final String RESPONSE_CODE_SUCCESS = "S20000";
    public static final String BEARER_PREFIX = "Bearer ";

    /* validation */
    public static final String NOT_NULL_MESSAGE = "필수 입력값이 누락되었습니다.";

    public static final int MIN_PAGE_NUMBER = 1;
    public static final int MAX_PAGE_NUMBER = 100;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 1000;
}
