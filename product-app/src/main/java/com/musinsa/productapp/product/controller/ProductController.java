package com.musinsa.productapp.product.controller;

import com.musinsa.productapp.product.model.dto.CategoryWithLowestPriceProductResponseDTO;
import com.musinsa.productapp.product.model.dto.CategoryWithMinMaxPriceResponseDTO;
import com.musinsa.productapp.product.model.dto.LowestPriceBrandWithCategoryResponseDTO;
import com.musinsa.productapp.product.model.dto.ProductRequestDTO;
import com.musinsa.productapp.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/category/lowest-price")
    public ResponseEntity<CategoryWithLowestPriceProductResponseDTO> getCategoriesWithLowestPriceProducts() {
        return ResponseEntity.ok().body(productService.getCategoriesWithLowestPriceProducts());
    }

    @GetMapping("/brand/lowest-price")
    public ResponseEntity<LowestPriceBrandWithCategoryResponseDTO> getLowestPriceBrandWithCategory() {
        return ResponseEntity.ok().body(productService.getLowestPriceBrandWithCategory());
    }

    @GetMapping("/category-min-max-price/{categoryName}")
    public ResponseEntity<CategoryWithMinMaxPriceResponseDTO> getCategoryWithMinMaxPrice(@PathVariable String categoryName) {
        return ResponseEntity.ok().body(productService.getCategoryWithMinMaxPrice(categoryName));
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        productService.saveProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id,@RequestBody ProductRequestDTO productRequestDTO) {
        productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}