package me.jisung.springplayground.product.controller;

import jakarta.validation.Valid;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.product.data.ProductDTO;
import me.jisung.springplayground.product.mapper.ProductStructMapper;
import me.jisung.springplayground.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponse<ProductDTO> create(@Valid @RequestBody ProductDTO productDTO) {
        return success(ProductStructMapper.INSTANCE.toDTO(productService.create(productDTO)));
    }

}
