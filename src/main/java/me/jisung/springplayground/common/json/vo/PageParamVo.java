package me.jisung.springplayground.common.json.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import me.jisung.springplayground.common.constant.ApiConst;

@Getter
public class PageParamVo {

    @Min(value = ApiConst.MIN_PAGE_NUMBER,  message = "Invalid page number.")
    @Max(value = ApiConst.MAX_PAGE_NUMBER,  message = "Invalid page number.")
    private final Integer page;

    @Min(value = ApiConst.MIN_PAGE_SIZE,    message = "Invalid page size.")
    @Max(value = ApiConst.MAX_PAGE_SIZE,    message = "Invalid page size.")
    private final Integer size;

    protected PageParamVo(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

}
