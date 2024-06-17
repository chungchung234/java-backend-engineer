package com.musinsa.productapp.product.repository;

import com.musinsa.productapp.product.model.entity.Category;
import com.musinsa.productapp.product.model.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    @Override
//    @EntityGraph(attributePaths = {"category", "brand"})
//    List<Product> findAll();
//
//    @EntityGraph(attributePaths = {"category", "brand"})
//    List<Product> findAllByOrderByIdDesc();

    @EntityGraph(attributePaths = {"category", "brand"})
    Optional<Product> findTopByCategoryOrderByPriceAscIdDesc(Category category);
}