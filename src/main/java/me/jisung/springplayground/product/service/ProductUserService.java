package me.jisung.springplayground.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.product.entity.ProductEntity;
import me.jisung.springplayground.product.entity.ProductUserEntity;
import me.jisung.springplayground.product.entity.ProductUserPK;
import me.jisung.springplayground.product.repository.ProductUserRepository;
import me.jisung.springplayground.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ProductUserService")
public class ProductUserService {

    private final ProductUserRepository productUserRepository;

    @Transactional
    public void create(ProductEntity product, UserEntity user) {
        ProductUserPK id = new ProductUserPK(product, user);
        ProductUserEntity productUser = new ProductUserEntity(id, product, user);
        productUserRepository.save(productUser);
    }

}
