package com.musinsa.productapp.product.repository;

import com.musinsa.productapp.product.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Override
    List<Category> findAll();

    List<Category> getAllByOrderByIdDesc();
}