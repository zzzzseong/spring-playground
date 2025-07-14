package me.jisung.springplayground.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.jisung.springplayground.common.util.StringUtil;
import me.jisung.springplayground.product.data.ProductSearchCond;
import me.jisung.springplayground.product.entity.ProductEntity;
import me.jisung.springplayground.product.entity.QProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() { queryFactory = new JPAQueryFactory(entityManager); }



    public List<ProductEntity> findAll(ProductSearchCond cond) {
        QProductEntity p = QProductEntity.productEntity;

        BooleanBuilder where = new BooleanBuilder();
        if(!StringUtil.isEmpty(cond.getName())) where.and(p.name.eq(cond.getName()));

        return queryFactory
                .select(p)
                .from(p)
                .offset(cond.getOffset())
                .limit(cond.getLimit())
                .orderBy(this.getOrderBy(cond))
                .fetch();
    }

    private OrderSpecifier<?> getOrderBy(ProductSearchCond cond) {
        QProductEntity p = QProductEntity.productEntity;

        boolean isAsc = "ASC".equalsIgnoreCase(cond.getSortDirection());

        switch (cond.getSortBy()) {
            case "name": return isAsc ? p.name.asc() : p.name.desc();
            default: return isAsc ? p.createdAt.asc() : p.createdAt.desc();
        }
    }
}
