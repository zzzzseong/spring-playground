package me.jisung.springplayground.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jisung.springplayground.user.entity.UserEntity;

@Getter
@Entity(name="product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUserEntity {

    @EmbeddedId
    private ProductUserPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @MapsId("productId")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private UserEntity user;

    public ProductUserEntity(ProductUserPK id, ProductEntity product, UserEntity user) {
        this.id = id;
        this.product = product;
        this.user = user;
    }
}
