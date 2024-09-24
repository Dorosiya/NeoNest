package com.shyu.NeoNest.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shyu.NeoNest.domain.Cart;
import com.shyu.NeoNest.dto.response.CartsDto;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.shyu.NeoNest.domain.QCart.cart;

public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CartRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CartsDto> getCartsDto(Long memberId) {
        return queryFactory
                .select(Projections.constructor(
                        CartsDto.class,
                        cart.cartId,
                        cart.member.memberId,
                        cart.product.productId,
                        cart.product.name,
                        cart.price,
                        cart.product.image.storeFileName.as("image"),
                        cart.quantity
                ))
                .from(cart)
                .where(cart.member.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public Optional<Cart> getCarts(Long memberId, Long cartId) {

        Cart getCart = queryFactory
                .selectFrom(cart)
                .where(cart.cartId.eq(cartId),
                        cart.member.memberId.eq(memberId))
                .fetchOne();

        return Optional.ofNullable(getCart);
    }

    @Override
    public void deleteCartById(Long memberId, Long cartId) {
        queryFactory
                .delete(cart)
                .where(cart.cartId.eq(cartId),
                        cart.member.memberId.eq(memberId))
                .execute();
    }

    @Override
    public void deleteCartsItemByIds(Long memberId, List<Long> cartItemIds) {
        queryFactory
                .delete(cart)
                .where(cart.cartId.in(cartItemIds),
                        cart.member.memberId.eq(memberId))
                .execute();
    }
}
