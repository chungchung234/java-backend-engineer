package com.musinsa.productapp.product.service;

import com.musinsa.productapp.product.model.dto.BrandDTO;
import com.musinsa.productapp.product.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    /*
구현4
    브랜드 저장

*/
    @Transactional
    public Long saveBrand( BrandDTO brandDTO) {

        brandRepository.findById(brandDTO.getBrandId()).orElseThrow(NoSuchElementException::new);

        return brandRepository.save(brandDTO.toEntity()).getId();

    }
    @Transactional
    public Long updateBrand(Long id, BrandDTO brandDTO) {
        brandRepository.findById(brandDTO.getBrandId()).orElseThrow(NoSuchElementException::new);

        return brandRepository.save(brandDTO.toEntity()).getId();
    }

    @Transactional
    public void deleteBrand(Long id) {
        brandRepository.findById(id).orElseThrow(NoSuchElementException::new);
        brandRepository.deleteById(id);
    }
}
