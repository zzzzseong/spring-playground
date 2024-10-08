package me.jisung.springplayground.common.json;

import lombok.Builder;

@Builder
public class ApiResponse<T> {
    private String result;
    private String code;
    private T data;
}
