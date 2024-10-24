package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Product;

import java.util.List;

public interface OrderProductRepositoryCustom {

    boolean existsOrderItem(Long orderId, Long productId);

}
