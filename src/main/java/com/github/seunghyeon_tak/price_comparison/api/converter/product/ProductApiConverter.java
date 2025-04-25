package com.github.seunghyeon_tak.price_comparison.api.converter.product;

import com.github.seunghyeon_tak.price_comparison.common.annotation.Converter;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductDetailDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductPriceDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductsDto;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Converter
public class ProductApiConverter {
    public ProductsDto toResponse(ProductEntity productEntity, BigDecimal price) {
        return ProductsDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .purchaseUrl(productEntity.getPurchaseUrl())
                .description(productEntity.getDescription())
                .price(price)
                .createdAt(productEntity.getCreatedAt())
                .build();
    }

    public Page<ProductsDto> toResponsePage(Page<ProductEntity> page, Map<Long, BigDecimal> priceMap) {
        return page.map(productEntity ->
                toResponse(productEntity, priceMap.get(productEntity.getId()))
        );
    }

    public ProductDetailDto toDetailResponse(
            ProductEntity productEntity,
            String categoryName,
            List<String> productImages,
            List<ProductPriceDto> productPriceDtoList,
            Integer lowestPrice
    ) {

        return ProductDetailDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .category(categoryName)
                .lowestPrice(lowestPrice)
                .description(productEntity.getDescription())
                .images(productImages)
                .stores(productPriceDtoList)
                .createdAt(productEntity.getCreatedAt())
                .build();
    }
}
