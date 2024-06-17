package com.musinsa.productapp.product.model.dto;

import com.musinsa.productapp.product.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductRequestDTO {
    private Long id;

    private CategoryDTO categoryDTO;

    private BrandDTO brandDTO;

    private int price;

    public Product toEntitiy (){
        return Product.builder()
                .category(this.categoryDTO.toEntity())
                .brand(this.brandDTO.toEntity())
                .id(this.id)
                .price(this.price)
                .build();
    }
}
