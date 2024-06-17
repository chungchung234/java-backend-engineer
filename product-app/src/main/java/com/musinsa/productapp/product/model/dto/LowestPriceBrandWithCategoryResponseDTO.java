package com.musinsa.productapp.product.model.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LowestPriceBrandWithCategoryResponseDTO {
    private String brandName;
    private List<CategoryWithPriceDTO> categoryWithPriceDTO;
    private String totalPrice;
}
