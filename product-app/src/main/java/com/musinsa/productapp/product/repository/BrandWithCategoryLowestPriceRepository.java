package com.musinsa.productapp.product.repository;

import com.musinsa.productapp.product.model.entity.Brand;
import com.musinsa.productapp.product.model.entity.BrandWithCategoryLowestPrice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandWithCategoryLowestPriceRepository extends JpaRepository<BrandWithCategoryLowestPrice, Long> {
    @EntityGraph(attributePaths = {"brand"})
    Optional<BrandWithCategoryLowestPrice> findTopByBrand(Brand brand);

}
