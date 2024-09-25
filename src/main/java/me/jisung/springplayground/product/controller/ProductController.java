package me.jisung.springplayground.product.controller;

import lombok.RequiredArgsConstructor;
import me.jisung.springplayground.product.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    public void create() {
        productService.create();
    }
}
