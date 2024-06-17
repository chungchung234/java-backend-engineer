package com.musinsa.productapp.product.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
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
    protected BrandWithCategoryLowestPrice(Long id, Brand brand, int totalCategoryMinPrice) {
        this.id = id;
        this.brand = brand;
        this.totalCategoryMinPrice = totalCategoryMinPrice;
    }
}