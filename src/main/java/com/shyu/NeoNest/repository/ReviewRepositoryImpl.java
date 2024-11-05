package com.shyu.NeoNest.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shyu.NeoNest.dto.response.ProductReviewDetailDto;
import com.shyu.NeoNest.dto.response.ProductReviewInfoDto;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static com.shyu.NeoNest.domain.QMember.member;
import static com.shyu.NeoNest.domain.QReview.review;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ProductReviewInfoDto findProductReviewInfo(Long productId) {
        // 리뷰 리스트 조회
        List<ProductReviewDetailDto> individualReviews = queryFactory
                .select(Projections.constructor(ProductReviewDetailDto.class,
                        member.memberName,
                        review.rating,
                        review.comment,
                        review.createdDate.stringValue()))
                .from(review)
                .join(member).on(review.member.memberId.eq(member.memberId))
                .where(review.product.productId.eq(productId))
                .fetch();

        // 평점 및 리뷰 개수 집계
        Tuple result = queryFactory
                .select(
                        review.rating.avg(),
                        review.count())
                .from(review)
                .where(review.product.productId.eq(productId))
                .fetchOne();

        // 집계 값 가져오기
        Double averageRating = result.get(review.rating.avg());
        Long ratingCount = result.get(review.count());

        // DTO 조립
        return new ProductReviewInfoDto(individualReviews, ratingCount, averageRating);
    }
}
