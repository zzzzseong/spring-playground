package me.jisung.springplayground.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.jisung.springplayground.common.annotation.ExcelColumn;
import me.jisung.springplayground.common.entity.BaseEntity;

import java.io.Serializable;


@Builder
@Entity(name="product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExcelColumn(headerName = "상품명")
    private String name;

    @ExcelColumn(headerName = "상품가격")
    private Integer price;
}