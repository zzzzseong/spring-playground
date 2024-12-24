package me.jisung.springplayground.common.json.vo;

import lombok.Getter;

/**
 * GET 으로 받아야하는 요청에 대한 조건은 모두 query param or path variable 로 받아야 한다.
 * */
@Getter
public class SearchParamVo extends PageParamVo {

    private final String query;

    protected SearchParamVo(Integer page, Integer size, String query) {
        super(page, size);
        this.query = query;
    }
}
