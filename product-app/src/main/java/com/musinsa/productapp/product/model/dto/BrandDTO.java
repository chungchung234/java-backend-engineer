package com.musinsa.productapp.product.model.dto;

import com.musinsa.productapp.product.model.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BrandDTO {
    private Long brandId;
    private String brandName;

    public Brand toEntity() {
        return Brand.builder()
                .name(this.brandName)
                .id(this.brandId)
                .build();
    }
}
