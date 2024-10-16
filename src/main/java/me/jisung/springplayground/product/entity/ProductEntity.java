package me.jisung.springplayground.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.jisung.springplayground.common.BaseEntity;
import me.jisung.springplayground.common.annotation.ExcelColumn;

@Entity
@Builder
@Table(name = "PRODUCT")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExcelColumn(headerName = "상품명")
    @Column(nullable = false)
    private String name;

    @ExcelColumn(headerName = "상품가격")
    @Column(nullable = false)
    private Integer price;
}