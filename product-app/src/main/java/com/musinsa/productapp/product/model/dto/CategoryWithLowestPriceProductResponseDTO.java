package com.musinsa.productapp.product.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CategoryWithLowestPriceProductResponseDTO {

    List<ProductDTO> productList;

    String totalPrice;
}
