package com.musinsa.productapp.product.service;

import com.musinsa.productapp.product.model.dto.LowestPriceResponseDTO;
import com.musinsa.productapp.product.model.dto.ProductDTO;
import com.musinsa.productapp.product.model.entity.Product;
import com.musinsa.productapp.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public LowestPriceResponseDTO getCategoriesWithLowestPriceProducts() {
        List<Product> allProducts = productRepository.findAllByOrderByIdDesc();
        List<Product> categoriesWithLowestPriceProducts = getCategoriesWithLowestPriceProducts(allProducts);
        int totalPrice = getTotalPriceOfProducts(categoriesWithLowestPriceProducts);
        return LowestPriceResponseDTO.builder()
                .productList(categoriesWithLowestPriceProducts.stream().map(ProductDTO::from).collect(Collectors.toList()))
                .totalPrice(totalPrice)
                .build();
    }

    private int getTotalPriceOfProducts(List<Product> prooducts) {
        return prooducts.stream().mapToInt(Product::getPrice).sum();
    }

    private static List<Product> getCategoriesWithLowestPriceProducts(List<Product> allProducts) {
        return allProducts.stream()
                .collect(Collectors.groupingBy(product -> product.getCategory().getId()))
                .values().stream()
                .map(products -> products.stream().min(Comparator.comparing(Product::getPrice)).orElseThrow(NoSuchElementException::new))
                .sorted(Comparator.comparing(product -> product.getCategory().getId()))
                .toList();
    }
}