package me.jisung.springplayground.product.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.jisung.springplayground.common.json.SearchCond;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProductSearchCond extends SearchCond {
    private String name;
}
