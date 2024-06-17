package com.musinsa.productapp.product.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category_lowest_price_product")
public class CategoryLowestPriceProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    protected CategoryLowestPriceProduct(Long id ,Product product) {
        this.id = id;
        this.product = product;
    }
}