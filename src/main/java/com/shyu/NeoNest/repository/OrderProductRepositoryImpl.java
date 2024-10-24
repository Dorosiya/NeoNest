package com.shyu.NeoNest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.shyu.NeoNest.domain.QOrderProduct.orderProduct;

public class OrderProductRepositoryImpl implements OrderProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public boolean existsOrderItem(Long orderId, Long productId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(orderProduct)
                .where(orderProduct.order.orderId.eq(orderId)
                        .and(orderProduct.product.productId.eq(productId)))
                .fetchFirst();

        return fetchOne != null;
    }
}
