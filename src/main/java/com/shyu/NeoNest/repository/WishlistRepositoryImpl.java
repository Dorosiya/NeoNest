package com.shyu.NeoNest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.shyu.NeoNest.domain.QWishlist.wishlist;

public class WishlistRepositoryImpl implements WishlistRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WishlistRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public boolean checkIfInWishlist(Long memberId, Long productId) {
        Integer result = queryFactory
                .selectOne()
                .from(wishlist)
                .where(wishlist.member.memberId.eq(memberId),
                        wishlist.product.productId.eq(productId))
                .fetchOne();

        return result != null;
    }
}
