package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Order;
import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, ReviewRepositoryCustom {

    boolean existsByMemberAndProductAndOrder(Member member, Product product, Order order);

}
