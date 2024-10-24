package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {



}
