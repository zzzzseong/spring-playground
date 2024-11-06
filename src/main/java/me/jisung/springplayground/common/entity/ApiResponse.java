package me.jisung.springplayground.common.entity;

import static me.jisung.springplayground.common.constant.ApiConstant.RESPONSE_CODE_SUCCESS;
import static me.jisung.springplayground.common.constant.ApiConstant.RESPONSE_RESULT_FAIL;
import static me.jisung.springplayground.common.constant.ApiConstant.RESPONSE_RESULT_SUCCESS;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private String result;
    private String code;
    private T data;

    public static <T> ApiResponse<T> success() {
        return success(null);
    }
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(RESPONSE_RESULT_SUCCESS, RESPONSE_CODE_SUCCESS, data);
    }

    public static <T> ApiResponse<T> fail(String code, T data) {
        return new ApiResponse<>(RESPONSE_RESULT_FAIL, code, data);
    }
}
