package com.musinsa.productapp.product.service;

import com.musinsa.productapp.PriceUtils;
import com.musinsa.productapp.product.model.dto.*;
import com.musinsa.productapp.product.model.entity.*;
import com.musinsa.productapp.product.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final CategoryLowestPriceProductRepository categoryLowestPriceProductRepository;
    private final BrandWithCategoryLowestPriceRepository brandWithCategoryLowestPriceProductRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, BrandRepository brandRepository, CategoryLowestPriceProductRepository categoryLowestPriceProductRepository, BrandWithCategoryLowestPriceRepository brandWithCategoryLowestPriceProductRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.categoryLowestPriceProductRepository = categoryLowestPriceProductRepository;
        this.brandWithCategoryLowestPriceProductRepository = brandWithCategoryLowestPriceProductRepository;
    }




    /*
        구현1
        카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API

        구현 내역 
        v1
            API가 요청 시 마다 상품 전체에 대한 풀 스캔
            → 최저가 비교는 대량의 상품이 존재할 시 수행시간, 서버 부하 등 문제
        v2
            해결방법 카테고리별 최저가 상품을 Caching 하는 테이블구성
            Caching 시점
            1. 상품 등록 시 카테고리별 최저가 비교 후 최저가 데이터 저장 (비동기 처리)
            2. 캐시 테이블 데이터 없을 시 product 테이블 조회 및 캐시 테이블 업데이트 (redis cache hit 방식 응용)
        v3 (미구현)
            테이블로서 디스크에 입력된 데이터를 In Memory 방식을 사용하는 JPA 2차 Cache 를 활용하면 코드 단순화 및 속도 개선 가능

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
    public CategoryWithLowestPriceProductResponseDTO getCategoriesWithLowestPriceProducts() {
        //모든 카테고리 조회
        List<Category> allCategories = categoryRepository.getAllByOrderByIdDesc();

        //카테고리별 최저 상품 데이터 조회 -> 데이터 없을 시 데이터 생성 및 조회 해서 가져온다
        List<Product> products = allCategories.stream().map(this::getLowestPriceProduct).toList();

        return CategoryWithLowestPriceProductResponseDTO.builder()
                .productList(getProductDTOs(products))
                .totalPrice(getTotalPriceOfProducts(products))
                .build();
    }

    private Product getLowestPriceProduct(Category category) {
        //최저가 데이터 없을 시 최저가 데이터 생성 및 조회
        return getCategoryLowestPriceProduct(category).getProduct();
    }

    private CategoryLowestPriceProduct getCategoryLowestPriceProduct(Category category) {
        return categoryLowestPriceProductRepository.findByProductCategory(category).orElse(saveCategoryLowestPriceProducts(category));
    }

    private List<ProductDTO> getProductDTOs(List<Product> products) {
        // Entity -> DTO 변환
        return products.stream().map(ProductDTO::from).collect(Collectors.toList());
    }

    private String getTotalPriceOfProducts(List<Product> prooducts) {
        //total price 계산
        return PriceUtils.formatPrice(prooducts.stream().mapToInt(Product::getPrice).sum());
    }

    public CategoryLowestPriceProduct saveCategoryLowestPriceProducts(Category category) {
        //카테고리 최저가 상품 조회
        CategoryLowestPriceProduct categoryLowestPriceProduct = CategoryLowestPriceProduct.builder()
                .product(productRepository.findTopByCategoryOrderByPriceAscIdDesc(category).orElse(null))
                .build();
        //조회 상품 최저가 상품 테이블 저장
        categoryLowestPriceProductRepository.save(categoryLowestPriceProduct);
        return categoryLowestPriceProduct;
    }

    /*
        구현2
        단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API

        *DDB의 document 구조 사용하여 브랜드별 최저가 총액 및 카테고리 정보 저장시 조회 성능 개선 여지 있을 것으로 보인다
        *jpql 로 쿼리 성능 개선 가능할 것으로 예상
     */
    public LowestPriceBrandWithCategoryResponseDTO getLowestPriceBrandWithCategory(){
        //모든 브랜드 조회
        List<Brand> allBrands = brandRepository.findAll();

        //브랜드별 최저가 비교 -> 데이터 없을 시 데이터 생성 및 조회 해서 가져온다
        Brand lowestPriceBrand = allBrands.stream()
                .min(Comparator.comparing(this::getTotalCategoryMinPrice))
                .orElseThrow(NoSuchElementException::new);

        //최저가 브랜드의 카테고리별 최저 가격 및 총 가격 생성 반환
        return LowestPriceBrandWithCategoryResponseDTO.builder()
                .brandName(lowestPriceBrand.getName())
                .categoryWithPriceDTO(getCategoryWithPriceDTO(lowestPriceBrand))
                .totalPrice(getTotalCategoryMinPrice(lowestPriceBrand))
                .build();
    }

    //카테고리별 최저 가격 반환
    private List<CategoryWithPriceDTO> getCategoryWithPriceDTO(Brand lowestPriceBrand) {
        return productRepository.findByBrand(lowestPriceBrand).stream()
                .collect(Collectors.groupingBy(Product::getCategory))
                .values().stream()
                .map(products -> products.stream().min(Comparator.comparing(Product::getPrice)).orElseThrow(NoSuchElementException::new))
                .sorted(Comparator.comparing(product -> product.getCategory().getId()))
                .map(ProductService::getCategoryWithPriceDTO)
                .toList();
    }

    //카테고리명, 최저가 반환
    private static CategoryWithPriceDTO getCategoryWithPriceDTO(Product product) {
        return CategoryWithPriceDTO.builder()
                .price(PriceUtils.formatPrice(product.getPrice()))
                .categoryName(product.getCategory().getName())
                .build();
    }

    //브랜드 카테고리 별 최저가 종합 반환
    private String getTotalCategoryMinPrice(Brand brand){
        return PriceUtils.formatPrice(brandWithCategoryLowestPriceProductRepository.findTopByBrand(brand).orElse(saveBrandLowestPrice(brand)).getTotalCategoryMinPrice());
    }

    //브랜드 카테고리 별 최저가 저장 및 반환
    private BrandWithCategoryLowestPrice saveBrandLowestPrice(Brand brand) {
        BrandWithCategoryLowestPrice brandWithCategoryLowestPrice = BrandWithCategoryLowestPrice.builder()
                .brand(brand)
                .totalCategoryMinPrice(getBrandWithTotalCategoryMinPrice(brand))
                .build();
        brandWithCategoryLowestPriceProductRepository.save(brandWithCategoryLowestPrice);
        return brandWithCategoryLowestPrice;
    }

    //브랜드별 카테고리별 최저가 총합 조회
    private int getBrandWithTotalCategoryMinPrice(Brand brand) {
        return productRepository.findByBrand(brand).stream()
                .collect(Collectors.groupingBy(Product::getCategory))
                .values().stream()
                .mapToInt(products -> products.stream().min(Comparator.comparing(Product::getPrice)).orElseThrow(NoSuchElementException::new).getPrice())
                .sum();
    }

        /*
        구현3
        카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API

         */
    public CategoryWithMinMaxPriceResponseDTO getCategoryWithMinMaxPrice(String categoryName){
        return CategoryWithMinMaxPriceResponseDTO.builder()
                .categoryName(categoryName)
                .minPrice(getBrandWithPriceDTO(productRepository.findTopByCategoryNameOrderByPriceAsc(categoryName)))
                .maxPrice(getBrandWithPriceDTO(productRepository.findTopByCategoryNameOrderByPriceDesc(categoryName)))
                .build();
    }

    private BrandWithPriceDTO getBrandWithPriceDTO(Product product) {
        return BrandWithPriceDTO.builder()
                .brand(product.getBrand().getName())
                .price(PriceUtils.formatPrice(product.getPrice()))
                .build();
    }

    /*
    구현4
        상품 저장, 업데이트 시 캐시 테이블 업데이트 조건 추가

    */
    @Transactional
    public Long saveProduct(ProductRequestDTO productRequestDTO) {

        brandRepository.findById(productRequestDTO.getBrandDTO().getBrandId()).orElseThrow(NoSuchElementException::new);
        categoryRepository.findById(productRequestDTO.getCategoryDTO().getCategoryId()).orElseThrow(NoSuchElementException::new);
        updateCateogoryLowestPriceProduct(productRequestDTO);
        updateLowestPriceBrand(productRequestDTO);
        return productRepository.save(productRequestDTO.toEntitiy()).getId();

    }

    private void updateLowestPriceBrand(ProductRequestDTO productRequestDTO) {
        saveBrandLowestPrice(productRequestDTO.getBrandDTO().toEntity());
    }

    private void updateCateogoryLowestPriceProduct(ProductRequestDTO productRequestDTO) {
        CategoryLowestPriceProduct lowestPriceProduct = getCategoryLowestPriceProduct(productRequestDTO.getCategoryDTO().toEntity());
        if ( productRequestDTO.getPrice() < lowestPriceProduct.getProduct().getPrice()){
            lowestPriceProduct.setProduct(productRequestDTO.toEntitiy());
        }
        ;
    }

    @Transactional
    public Long updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        brandRepository.findById(productRequestDTO.getBrandDTO().getBrandId()).orElseThrow(NoSuchElementException::new);
        categoryRepository.findById(productRequestDTO.getCategoryDTO().getCategoryId()).orElseThrow(NoSuchElementException::new);
        updateCateogoryLowestPriceProduct(productRequestDTO);
        updateLowestPriceBrand(productRequestDTO);
        return productRepository.save(productRequestDTO.toEntitiy()).getId();
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        productRepository.deleteById(id);
    }
}
