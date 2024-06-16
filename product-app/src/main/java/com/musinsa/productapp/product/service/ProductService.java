package com.musinsa.productapp.product.service;

import com.musinsa.productapp.product.model.entity.Product;
import com.musinsa.productapp.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getCategoriesWithLowestPriceProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .collect(Collectors.groupingBy(product -> product.getCategory().getName()))
                .values().stream()
                .map(products -> products.stream().min((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice())).orElse(null))
                .sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                .collect(Collectors.toList());
    }
}