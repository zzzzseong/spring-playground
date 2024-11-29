package me.jisung.springplayground.common.json;

import lombok.Getter;

@Getter
public class SearchRequestDto extends PageRequestDto {
    private String query;
}
