package me.jisung.springplayground.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.product.data.ProductRequest;
import me.jisung.springplayground.product.data.ProductSearchCond;
import me.jisung.springplayground.product.entity.ProductEntity;
import me.jisung.springplayground.product.mapper.ProductStructMapper;
import me.jisung.springplayground.product.repository.ProductQueryRepository;
import me.jisung.springplayground.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("v1")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceV1 implements ProductService {

    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;



    @Override
    @Transactional
    public ProductEntity create(ProductRequest productRequest) {
        ProductEntity product = ProductStructMapper.INSTANCE.toEntity(productRequest);
        productRepository.save(product);
        return product;
    }

    @Override
    public List<ProductEntity> findAll(ProductSearchCond cond) {
        log.info("cond = {}", cond);
        return productQueryRepository.findAll(cond);
    }

}
