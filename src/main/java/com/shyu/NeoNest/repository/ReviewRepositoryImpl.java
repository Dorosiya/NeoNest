package com.shyu.NeoNest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shyu.NeoNest.domain.QProduct;
import com.shyu.NeoNest.domain.QReview;
import com.shyu.NeoNest.dto.response.ReviewInfoDto;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.shyu.NeoNest.domain.QProduct.product;
import static com.shyu.NeoNest.domain.QReview.review;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
