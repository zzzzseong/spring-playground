package me.jisung.springplayground.product.service;

import me.jisung.springplayground.product.data.ProductRequest;
import me.jisung.springplayground.product.data.ProductSearchCond;
import me.jisung.springplayground.product.entity.ProductEntity;

import java.util.List;

public interface ProductService {

    /**
     * 상품 생성
     * @param productRequest 상품 생성 요청 정보
     * @return 생성된 상품 정보
     * */
    ProductEntity create(ProductRequest productRequest);


    List<ProductEntity> findAll(ProductSearchCond cond);

}
