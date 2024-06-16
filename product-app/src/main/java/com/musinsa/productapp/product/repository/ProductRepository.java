package com.musinsa.productapp.product.repository;

import com.musinsa.productapp.product.model.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = {"category", "brand"})
    List<Product> findAll();

    @EntityGraph(attributePaths = {"category", "brand"})
    List<Product> findAllByOrderByIdDesc();
}