package com.musinsa.productapp.product.repository;

import com.musinsa.productapp.product.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Override
    List<Brand> findAll();
}
