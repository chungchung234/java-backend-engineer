package com.musinsa.productapp.product.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "brand_with_category_lowest_price")
public class BrandWithCategoryLowestPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(nullable = false)
    private int totalCategoryMinPrice;

    @Builder
    protected BrandWithCategoryLowestPrice(Brand brand, int totalCategoryMinPrice) {
        this.brand = brand;
        this.totalCategoryMinPrice = totalCategoryMinPrice;
    }
}