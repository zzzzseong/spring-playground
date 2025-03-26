package me.jisung.springplayground.product.service;

import me.jisung.springplayground.product.data.ProductDTO;
import me.jisung.springplayground.product.entity.ProductEntity;

public interface ProductService {

    /**
     * 상품 생성
     * @param productDTO 상품 생성 요청 정보
     * @return 생성된 상품 정보
     * */
    ProductEntity create(ProductDTO productDTO);

}
