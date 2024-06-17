package com.musinsa.productapp.product.model.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategoryWithLowestPriceProductResponseDTO {

    List<ProductDTO> productList;

    String totalPrice;
}
