package me.jisung.springplayground.common.exception;

import lombok.Getter;
import me.jisung.springplayground.common.util.DateUtil;

@Getter
public class ApiErrorVo {
    private final String message;
    private final String timestamp;

    public ApiErrorVo(String message) {
        this.message = message;
        this.timestamp = DateUtil.now(DateUtil.DATE_TIME_FORMAT);
    }
}