package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDashboardRepository extends JpaRepository<Order, Long>, UserDashboardRepositoryCustom   {
}
