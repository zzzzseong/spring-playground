package me.jisung.springplayground.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.jisung.springplayground.user.entity.UserEntity;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUserPK implements Serializable {

    @Column(name = "promotion_id")
    private Long productId;

    @Column(name = "user_id")
    private Long userId;

    public ProductUserPK(ProductEntity product, UserEntity user) {
        this.productId = product.getId();
        this.userId = user.getId();
    }
}
