package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
