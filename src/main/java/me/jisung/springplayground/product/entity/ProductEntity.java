package me.jisung.springplayground.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.jisung.springplayground.common.annotation.ExcelColumn;
import me.jisung.springplayground.common.entity.SimpleBaseEntity;

import java.io.Serializable;

@Getter
@SuperBuilder
@Entity(name="product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends SimpleBaseEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExcelColumn(headerName = "상품명")
    private String name;

    @ExcelColumn(headerName = "상품가격")
    private Integer price;
}
