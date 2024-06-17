package com.musinsa.productapp.product.service;

import com.musinsa.productapp.PriceUtils;
import com.musinsa.productapp.product.model.dto.CategoryWithLowestPriceProductResponseDTO;
import com.musinsa.productapp.product.model.dto.ProductDTO;
import com.musinsa.productapp.product.model.entity.Category;
import com.musinsa.productapp.product.model.entity.CategoryLowestPriceProduct;
import com.musinsa.productapp.product.model.entity.Product;
import com.musinsa.productapp.product.repository.CategoryLowestPriceProductRepository;
import com.musinsa.productapp.product.repository.CategoryRepository;
import com.musinsa.productapp.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryLowestPriceProductRepository categoryLowestPriceProductRepository;


    /*
        구현1
        고려사항
        1번 고려사항 만족하는 개발 후 API가 요청 시 마다 상품 전체에 대한 풀 스캔
        → 최저가 비교는 대량의 상품이 존재할 시 수행시간, 서버 부하 등 문제

        해결방법 카테고리별 최저가 상품을 Caching 하는 테이블구성
        Caching 시점
        1. 상품 등록 시 카테고리별 최저가 비교 후 최저가 데이터 저장 (비동기 처리)
        2. 캐시 테이블 데이터 없을 시 product 테이블 조회 및 캐시 테이블 업데이트 (redis cache hit 방식 응용)

        *Redis or DDB 사용시 성능 개선 여지 있음
     */
    // 구현1 v1
//    private static List<Product> getCategoriesWithLowestPriceProducts(List<Product> allProducts) {
//        return allProducts.stream()
//                .collect(Collectors.groupingBy(product -> product.getCategory().getId()))
//                .values().stream()
//                .map(products -> products.stream().min(Comparator.comparing(Product::getPrice)).orElseThrow(NoSuchElementException::new))
//                .sorted(Comparator.comparing(product -> product.getCategory().getId()))
//                .toList();
//    }

    //구현1 v2
    @Transactional
    public CategoryWithLowestPriceProductResponseDTO getCategoriesWithLowestPriceProducts() {
        //모든 카테고리 호출
        List<Category> allCategories = categoryRepository.getAllByOrderByIdDesc();

        //카테고리별 최저 상품 데이터 호출 -> 데이터 없을 시 lowest 테이블 업데이트 및 조회 해서 가져온다
        List<Product> products = allCategories.stream().map(category -> categoryLowestPriceProductRepository.findByProductCategory(category).orElse(updateLowestPriceProducts(category)).getProduct()).toList();

        return CategoryWithLowestPriceProductResponseDTO.builder()
                .productList(getProductDTOs(products))
                .totalPrice(getTotalPriceOfProducts(products))
                .build();
    }

    private List<ProductDTO> getProductDTOs(List<Product> products) {
        // Entity -> DTO 변환
        return products.stream().map(ProductDTO::from).collect(Collectors.toList());
    }

    private String getTotalPriceOfProducts(List<Product> prooducts) {
        //total price 계산
        return PriceUtils.formatPrice(prooducts.stream().mapToInt(Product::getPrice).sum());
    }

    public CategoryLowestPriceProduct updateLowestPriceProducts(Category category) {
        //카테고리 최저가 상품 조회
        CategoryLowestPriceProduct categoryLowestPriceProduct = CategoryLowestPriceProduct.builder().product(productRepository.findTopByCategoryOrderByPriceAscIdDesc(category).orElseThrow(NoSuchElementException::new)).build();
        //조회 상품 최저가 상품 테이블 저장
        categoryLowestPriceProductRepository.save(categoryLowestPriceProduct);
        return categoryLowestPriceProduct;
    }

    /*
        구현2
        구현 1과 같은 캐시 구조을 사용하지만 비교 연산시 사용되는 브랜드별 총액을 저장 하여 구성

        *DDB의 document 구조 사용시 성능 개선 여지 있음
     */
    public
 }
