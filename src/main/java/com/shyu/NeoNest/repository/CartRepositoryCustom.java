package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Cart;
import com.shyu.NeoNest.dto.response.CartsDto;

import java.util.List;
import java.util.Optional;

public interface CartRepositoryCustom {

    List<CartsDto> getCartsDto(Long memberId);

    Optional<Cart> getCarts(Long memberId, Long cartId);

    void deleteCartById(Long memberId, Long cartId);

    void deleteCartsItemByIds(Long memberId, List<Long> cartItemIds);

    Optional<Cart> findByMemberIdAndProductId(Long memberId, Long productId);
}
