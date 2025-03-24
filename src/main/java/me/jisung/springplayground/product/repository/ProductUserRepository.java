package me.jisung.springplayground.product.repository;

import me.jisung.springplayground.product.entity.ProductUserEntity;
import me.jisung.springplayground.product.entity.ProductUserPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserRepository extends JpaRepository<ProductUserEntity, ProductUserPK> {
}
