package me.jisung.springplayground.common.util;


import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.constant.ApiConstant;
import me.jisung.springplayground.common.json.ApiResponse;

@Slf4j(topic = "ApiResponseUtil")
public class ApiResponseUtil {

    private ApiResponseUtil() { throw new IllegalStateException("utility class cannot be instantiated"); }

    public static String success() {
        return success(null);
    }

    public static <T> String success(T data) {
        String response = JsonUtil.toJson(ApiResponse.builder()
                .result(ApiConstant.RESPONSE_RESULT_SUCCESS)
                .code(ApiConstant.RESPONSE_CODE_SUCCESS)
                .data(data)
                .build()
        );
        log.info("[RESPONSE SUCCESS]: {}", response);
        return response;
    }

    public static <T> String fail(T data) {
        String response = JsonUtil.toJson(ApiResponse.builder()
                .result(ApiConstant.RESPONSE_RESULT_FAIL)
                .code(ApiConstant.RESPONSE_CODE_FAIL)
                .data(data)
                .build()
        );
        log.info("[RESPONSE FAIL]: {}", response);
        return response;
    }
}
