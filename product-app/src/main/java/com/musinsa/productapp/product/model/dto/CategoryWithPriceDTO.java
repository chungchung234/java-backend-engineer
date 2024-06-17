package com.musinsa.productapp.product.model.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategoryWithPriceDTO {
    private String categoryName;
    private String price;
}
