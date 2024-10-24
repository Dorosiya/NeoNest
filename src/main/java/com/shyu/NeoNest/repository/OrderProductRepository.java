package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>, OrderProductRepositoryCustom {
}
