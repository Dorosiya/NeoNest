package com.shyu.NeoNest.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shyu.NeoNest.domain.QMember;
import com.shyu.NeoNest.dto.response.UserDashboardDto;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.shyu.NeoNest.domain.QCart.cart;
import static com.shyu.NeoNest.domain.QMember.member;
import static com.shyu.NeoNest.domain.QOrder.order;
import static com.shyu.NeoNest.domain.QReview.review;

public class UserDashboardRepositoryImpl implements UserDashboardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserDashboardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public UserDashboardDto getUserDashboardDtoByMemberId(Long memberId) {
        UserDashboardDto result = queryFactory
                .select(Projections.constructor(UserDashboardDto.class,
                        order.count(),
                        JPAExpressions
                                .select(cart.count())
                                .from(cart)
                                .where(cart.member.memberId.eq(memberId)),
                        JPAExpressions
                                .select(review.count())
                                .from(review)
                                .where(review.member.memberId.eq(memberId))
                ))
                .from(order)
                .where(order.member.memberId.eq(memberId))
                .fetchOne();

        if (result == null) {
            result = new UserDashboardDto(0L, 0L, 0L);
        }

        return result;
    }
}
