package me.jisung.springplayground.common.json;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.jisung.springplayground.common.constant.ApiConst;
import me.jisung.springplayground.common.util.JsonUtil;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class SearchCond {

    @Builder.Default
    protected Integer page = ApiConst.DEFAULT_PAGE_NUMBER;

    @Builder.Default
    protected Integer count = ApiConst.DEFAULT_PAGE_SIZE;

    @Builder.Default
    protected String sortBy = ApiConst.DEFAULT_SORT_BY;

    @Builder.Default
    protected String sortDirection = ApiConst.DEFAULT_SORT_DIRECTION;



    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    public int getOffset() {
        return (page - 1) * count;
    }
    public int getLimit() {
        return count;
    }
}
