package com.musinsa.productapp.product.model.dto;

import com.musinsa.productapp.product.model.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;

    public Category toEntity() {
        return Category.builder()
                .name(this.categoryName)
                .id(this.categoryId)
                .build();
    }
}
