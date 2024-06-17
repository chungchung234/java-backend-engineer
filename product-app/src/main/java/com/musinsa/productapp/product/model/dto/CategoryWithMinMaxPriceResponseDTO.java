package com.musinsa.productapp.product.model.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategoryWithMinMaxPriceResponseDTO {
    private String categoryName;
    private BrandWithPriceDTO minPrice;
    private BrandWithPriceDTO maxPrice;
}
