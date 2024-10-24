package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.dto.response.AdminProductListDto;
import com.shyu.NeoNest.dto.response.EditProductDto;
import com.shyu.NeoNest.dto.response.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {

    Optional<Product> findByName(String name);

    List<ProductDto> searchByCond(String categoryCond, String sortCond);

    Optional<ProductDto> getByProductId(Long productId);

    List<Product> getProducts(List<Long> productIds);

    List<AdminProductListDto> getAdminProductListDto();

    Optional<EditProductDto> getEditProductById(Long productId);

}
