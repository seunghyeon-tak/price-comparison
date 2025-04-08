package com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ProductPriceDto {
    private String storeName;
    private Integer price;
    private String productUrl;
    private LocalDateTime lastUpdated;
}
