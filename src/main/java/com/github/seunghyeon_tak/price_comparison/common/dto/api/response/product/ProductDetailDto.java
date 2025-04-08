package com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ProductDetailDto {
    private Long id;
    private String name;
    private String category;
    private Integer lowestPrice;
    private String description;
    private List<String> images;
    private List<ProductPriceDto> stores;
    private LocalDateTime createdAt;
}
