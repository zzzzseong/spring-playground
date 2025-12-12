package me.jisung.springplayground.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResult<T> {
    private final Long totalElements;
    private final Long totalPages;
    private final Boolean hasNext;
    private final List<T> list;

    private SearchResult(Long totalElements, Long totalPages, Boolean hasNext, List<T> list) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.list = list;
    }

    public static <T> SearchResult<T> toResponse(long totalPages, List<T> list) {
        return new SearchResult<>(null, totalPages, null, list);
    }
    public static <T> SearchResult<T> toResponse(long totalElements, long totalPages, boolean hasNext, List<T> list) {
        return new SearchResult<>(totalElements, totalPages, hasNext, list);
    }

    /**
     * SearchResult 리스트 요소를 변환하는 map 메서드
     *
     * @param mapper 변환 함수
     * @param <R> 변환될 타입
     * @return 변환된 새로운 SearchResult
     */
    public <R> SearchResult<R> map(Function<T, R> mapper) {
        List<R> mappedList = this.list.stream()
                .map(mapper)
                .toList();
        
        return toResponse(this.totalElements, this.totalPages, this.hasNext, mappedList);
    }

}
