package com.github.seunghyeon_tak.price_comparison.api.service.product;

import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductApiService {
    private final ProductRepository productRepository;

    public Page<ProductEntity> getActiveProducts(Pageable pageable) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("페이징 요청 : {}", pageable);
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(productRepository.findAll(pageable));
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>");
        return productRepository.findByIsActiveTrue(pageable);
    }
}
