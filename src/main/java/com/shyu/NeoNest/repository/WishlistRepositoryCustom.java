package com.shyu.NeoNest.repository;

public interface WishlistRepositoryCustom {

    boolean checkIfInWishlist(Long memberId, Long productId);

}
