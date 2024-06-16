package com.musinsa.productapp.product.model.dto;

import com.musinsa.productapp.product.model.entity.Brand;
import com.musinsa.productapp.product.model.entity.Category;
import com.musinsa.productapp.product.model.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductDTO {
    private Long id;

    private Category category;

    private Brand brand;

    private int price;

    public static ProductDTO from(Product product){
        return ProductDTO.builder()
                .id(product.getId())
                .brand(product.getBrand())
                .category(product.getCategory())
                .price(product.getPrice())
                .build();
    }
}
