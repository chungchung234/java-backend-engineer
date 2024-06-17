package com.musinsa.productapp.product.repository;

import com.musinsa.productapp.product.model.entity.Brand;
import com.musinsa.productapp.product.model.entity.Category;
import com.musinsa.productapp.product.model.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    구현1 v1
//    @Override
//    @EntityGraph(attributePaths = {"category", "brand"})
//    List<Product> findAll();
//
//    @EntityGraph(attributePaths = {"category", "brand"})
//    List<Product> findAllByOrderByIdDesc();

    @EntityGraph(attributePaths = {"category", "brand"})
    Optional<Product> findTopByCategoryOrderByPriceAscIdDesc(Category category);

    List<Product> findByBrand(Brand brand);
    Product findTopByCategoryNameOrderByPriceAsc(String categoryName);
    Product findTopByCategoryNameOrderByPriceDesc(String categoryName);

}