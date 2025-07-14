package me.jisung.springplayground.product.data;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.jisung.springplayground.common.constant.ValidationConst;

@Getter
@Builder
@AllArgsConstructor
public class ProductRequest {

    @NotNull(message = ValidationConst.MESSAGE_NOT_NULL + "(name)")
    private String name;

    @NotNull(message = ValidationConst.MESSAGE_NOT_NULL + "(price)")
    private Integer price;

}
