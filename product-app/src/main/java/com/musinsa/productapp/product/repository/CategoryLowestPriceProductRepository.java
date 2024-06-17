package com.musinsa.productapp.product.repository;

import com.musinsa.productapp.product.model.entity.Category;
import com.musinsa.productapp.product.model.entity.CategoryLowestPriceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryLowestPriceProductRepository extends JpaRepository<CategoryLowestPriceProduct, Long> {
    Optional<CategoryLowestPriceProduct> findByProductCategory(Category category);
}