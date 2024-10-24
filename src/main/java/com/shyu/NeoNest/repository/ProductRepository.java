package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.dto.response.EditProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}
