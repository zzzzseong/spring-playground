package me.jisung.springplayground.common.json;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import me.jisung.springplayground.common.constant.ApiConst;

@Getter
public class PageRequestDto {

    @Min(value = ApiConst.MIN_PAGE_NUMBER,  message = "Invalid page number.")
    @Max(value = ApiConst.MAX_PAGE_NUMBER,  message = "Invalid page number.")
    private int page;

    @Min(value = ApiConst.MIN_PAGE_SIZE,    message = "Invalid page size.")
    @Max(value = ApiConst.MAX_PAGE_SIZE,    message = "Invalid page size.")
    private int size;
}
