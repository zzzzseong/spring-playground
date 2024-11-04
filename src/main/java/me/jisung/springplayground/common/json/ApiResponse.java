package me.jisung.springplayground.common.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static me.jisung.springplayground.common.constant.ApiConstant.*;

@Getter
@AllArgsConstructor
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
