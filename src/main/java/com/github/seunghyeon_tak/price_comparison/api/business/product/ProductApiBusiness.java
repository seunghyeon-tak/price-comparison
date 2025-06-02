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
import com.github.seunghyeon_tak.price_comparison.core.redis.RedisProductPriceCacheService;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Business
@RequiredArgsConstructor
public class ProductApiBusiness {
    private final ProductApiService productApiService;
    private final ProductApiConverter productApiConverter;
    private final ProductImageApiService productImageApiService;
    private final ProductPriceApiService productPriceApiService;
    private final RedisProductPriceCacheService redisProductPriceCacheService;

    @BusinessLoggable("상품 목록 비지니스")
    @LogException
    public Page<ProductsDto> list(Pageable pageable) {
        Page<ProductEntity> entities = productApiService.getActiveProducts(pageable);

        // 상품 ID 리스트 추출
        List<Long> productIds = entities.stream()
                .map(ProductEntity::getId)
                .toList();

        // 상품별 최신 가격 가져오기
        Map<Long, BigDecimal> priceMap = productPriceApiService.findLatestPriceByProductIds(productIds);

        return productApiConverter.toResponsePage(entities, priceMap);
    }

    @BusinessLoggable("상품 상세 비지니스")
    @LogException
    public ProductDetailDto detail(Long productId) {
        ProductEntity productEntity = productApiService.getProduct(productId);
        String categoryName = productApiService.getProductCategoryName(productId);
        List<String> imageUrls = productImageApiService.getProductImages(productId);
        List<ProductPriceDto> productPriceDtoList = productPriceApiService.getProductPriceDto(productId);

        // redis에서 최저가 검색
        BigDecimal cachePrice = productPriceApiService.getCachePrice(productId);
        if (cachePrice == null) {
            cachePrice = productPriceDtoList.stream()
                    .map(dto -> new BigDecimal(dto.getPrice()))
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            // 캐시 저장
            productPriceApiService.cachePrice(productId, cachePrice);
        }

        return productApiConverter.toDetailResponse(
                productEntity,
                categoryName,
                imageUrls,
                productPriceDtoList,
                cachePrice.intValue()
        );
    }
}
