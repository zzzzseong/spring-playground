package me.jisung.springplayground.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() { queryFactory = new JPAQueryFactory(entityManager); }

}
