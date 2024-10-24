package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long>, WishlistRepositoryCustom {

    Optional<Wishlist> findByMemberAndProduct(Member member, Product product);

}
