package com.musinsa.productapp.product.controller;

import com.musinsa.productapp.product.model.dto.CategoryWithLowestPriceProductResponseDTO;
import com.musinsa.productapp.product.model.dto.LowestPriceBrandWithCategoryResponseDTO;
import com.musinsa.productapp.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/category/lowest-price")
    public ResponseEntity<CategoryWithLowestPriceProductResponseDTO> getCategoriesWithLowestPriceProducts() {
        return ResponseEntity.ok().body(productService.getCategoriesWithLowestPriceProducts());
    }

    @GetMapping("/brand/lowest-price")
    public ResponseEntity<LowestPriceBrandWithCategoryResponseDTO> getLowestPriceBrandWithCategory() {
        return ResponseEntity.ok().body(productService.getLowestPriceBrandWithCategory());
    }
}