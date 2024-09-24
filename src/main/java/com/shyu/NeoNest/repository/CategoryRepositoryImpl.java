package com.shyu.NeoNest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shyu.NeoNest.domain.Category;
import com.shyu.NeoNest.domain.QCategory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.shyu.NeoNest.domain.QCategory.category;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Category> findByName(String categoryName) {
        Category findCategory = queryFactory
                .selectFrom(category)
                .where(category.categoryName.eq(categoryName))
                .fetchOne();

        return Optional.ofNullable(findCategory);
    }
}
