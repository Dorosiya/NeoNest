package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {
}
