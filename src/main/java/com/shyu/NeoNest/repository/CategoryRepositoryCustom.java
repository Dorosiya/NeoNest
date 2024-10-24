package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Category;

import java.util.Optional;

public interface CategoryRepositoryCustom {

    Optional<Category> findByName(String categoryName);

}
