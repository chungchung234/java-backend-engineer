package com.musinsa.productapp.product.controller;

import com.musinsa.productapp.product.model.dto.LowestPriceResponseDTO;
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

    @GetMapping("/lowest-price")
    public ResponseEntity<LowestPriceResponseDTO> getCategoriesWithLowestPriceProducts() {
        return ResponseEntity.ok().body(productService.getCategoriesWithLowestPriceProducts());
    }
}