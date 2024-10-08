package me.jisung.springplayground.common.constant;

public class ApiConstant {

    private ApiConstant() { throw new IllegalStateException("utility class cannot be instantiated"); }

    public static final String RESPONSE_RESULT_SUCCESS = "success";
    public static final String RESPONSE_RESULT_FAIL = "fail";

    public static final String RESPONSE_CODE_SUCCESS = "1000";
    public static final String RESPONSE_CODE_FAIL = "2000";

    public static final String BEARER_PREFIX = "Bearer ";
}
