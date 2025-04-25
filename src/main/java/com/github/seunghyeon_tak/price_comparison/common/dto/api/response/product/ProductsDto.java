package com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ProductsDto {
    private Long id;
    private String name;
    private String purchaseUrl;
    private String description;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
