package me.jisung.springplayground.common.util;


import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.constant.ApiConstant;
import me.jisung.springplayground.common.json.ApiResponse;

@Slf4j(topic = "ApiResponseUtil")
public class ApiResponseUtil {

    private static final String RESPONSE_RESULT_SUCCESS = "success";
    private static final String RESPONSE_RESULT_FAIL = "fail";

    private ApiResponseUtil() { throw new IllegalStateException("utility class can not be instantiated"); }

    public static String success() {
        return success(null);
    }
    public static <T> String success(T data) {
        return JsonUtil.toJson(ApiResponse.builder()
                .result(RESPONSE_RESULT_SUCCESS)
                .code(ApiConstant.RESPONSE_CODE_SUCCESS)
                .data(data)
                .build()
        );
    }

    public static <T> String fail(String code, T data) {
        return JsonUtil.toJson(ApiResponse.builder()
                .result(RESPONSE_RESULT_FAIL)
                .code(code)
                .data(data)
                .build()
        );
    }
}