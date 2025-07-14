package me.jisung.springplayground.product.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

    private String name;
    private Integer price;

}
