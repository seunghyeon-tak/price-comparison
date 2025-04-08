package com.github.seunghyeon_tak.price_comparison.api.business.product;

import com.github.seunghyeon_tak.price_comparison.api.converter.product.ProductApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductImageApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductPriceApiService;
import com.github.seunghyeon_tak.price_comparison.common.annotation.Business;
import com.github.seunghyeon_tak.price_comparison.common.annotation.BusinessLoggable;
import com.github.seunghyeon_tak.price_comparison.common.annotation.LogException;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductDetailDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductPriceDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductsDto;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Business
@RequiredArgsConstructor
public class ProductApiBusiness {
    private final ProductApiService productApiService;
    private final ProductApiConverter productApiConverter;
    private final ProductImageApiService productImageApiService;
    private final ProductPriceApiService productPriceApiService;

    @BusinessLoggable("상품 목록 비지니스")
    @LogException
    public Page<ProductsDto> list(Pageable pageable) {
        Page<ProductEntity> entities = productApiService.getActiveProducts(pageable);
        return productApiConverter.toResponsePage(entities);
    }

    @BusinessLoggable("상품 상세 비지니스")
    @LogException
    public ProductDetailDto detail(Long productId) {
        ProductEntity productEntity = productApiService.getProduct(productId);
        String categoryName = productApiService.getProductCategoryName(productId);
        List<String> imageUrls = productImageApiService.getProductImages(productId);
        List<ProductPriceDto> productPriceDtoList = productPriceApiService.getProductPriceDto(productId);

        Integer lowestPrice = productPriceDtoList.stream()
                .map(ProductPriceDto::getPrice)
                .min(Integer::compareTo)
                .orElse(0);

        return productApiConverter.toDetailResponse(
                productEntity,
                categoryName,
                imageUrls,
                productPriceDtoList,
                lowestPrice
        );
    }
}
