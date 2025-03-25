package me.jisung.springplayground.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.product.data.ProductDTO;
import me.jisung.springplayground.product.entity.ProductEntity;
import me.jisung.springplayground.product.mapper.ProductStructMapper;
import me.jisung.springplayground.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "ProductService")
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductEntity create(ProductDTO productDTO) {
        ProductEntity product = ProductStructMapper.INSTANCE.toEntity(productDTO);
        productRepository.save(product);
        return product;
    }

}
