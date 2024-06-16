package com.musinsa.productapp.product.controller;

import com.musinsa.productapp.product.model.entity.Product;
import com.musinsa.productapp.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/lowest-price")
    public List<Product> getCategoriesWithLowestPriceProducts() {
        return productService.getCategoriesWithLowestPriceProducts();
    }
}