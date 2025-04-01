package com.github.seunghyeon_tak.price_comparison.api.business.product;

import com.github.seunghyeon_tak.price_comparison.api.converter.product.ProductApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductApiService;
import com.github.seunghyeon_tak.price_comparison.common.annotation.Business;
import com.github.seunghyeon_tak.price_comparison.common.annotation.BusinessLoggable;
import com.github.seunghyeon_tak.price_comparison.common.annotation.LogException;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductsDto;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Business
@RequiredArgsConstructor
public class ProductApiBusiness {
    private final ProductApiService productApiService;
    private final ProductApiConverter productApiConverter;

    @BusinessLoggable("상품 목록 비지니스")
    @LogException
    public Page<ProductsDto> list(Pageable pageable) {
        Page<ProductEntity> entities = productApiService.getActiveProducts(pageable);
        return productApiConverter.toResponsePage(entities);
    }
}
