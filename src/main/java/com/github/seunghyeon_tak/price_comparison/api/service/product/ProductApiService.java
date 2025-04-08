package com.github.seunghyeon_tak.price_comparison.api.service.product;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductPriceDto;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.product.ProductResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductApiService {
    private final ProductRepository productRepository;

    public Page<ProductEntity> getActiveProducts(Pageable pageable) {
        return productRepository.findByIsActiveTrue(pageable);
    }

    public ProductEntity getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ProductResponseCode.PRODUCT_NOT_FOUND));
    }

    public String getProductCategoryName(Long productId) {
        String name = productRepository.findCategoryNameByProductId(productId);

        if (name == null) {
            throw new ApiException(ProductResponseCode.PRODUCT_CATEGORY_NULL);
        }

        return name;
    }

}
