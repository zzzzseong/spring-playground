package me.jisung.springplayground.product.controller;

import jakarta.validation.Valid;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.product.data.ProductRequest;
import me.jisung.springplayground.product.data.ProductResponse;
import me.jisung.springplayground.product.data.ProductSearchCond;
import me.jisung.springplayground.product.entity.ProductEntity;
import me.jisung.springplayground.product.mapper.ProductStructMapper;
import me.jisung.springplayground.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(@Qualifier("v1") ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ProductRequest> create(@Valid @RequestBody ProductRequest productRequest) {
        return success(ProductStructMapper.INSTANCE.toDTO(productService.create(productRequest)));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<ProductResponse>> getProducts(ProductSearchCond cond) {
        List<ProductEntity> products = productService.findAll(cond);
        return success(products.stream().map(ProductStructMapper.INSTANCE::toResponse).toList());
    }

}
