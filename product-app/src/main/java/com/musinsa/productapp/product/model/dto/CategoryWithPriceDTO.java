package com.musinsa.productapp.product.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryWithPriceDTO {
    private String categoryName;
    private String price;
}
