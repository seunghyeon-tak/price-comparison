package com.github.seunghyeon_tak.price_comparison.common.dto.api.response.price_alerts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class PriceAlertsDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal latestPrice;
    private BigDecimal targetPrice;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
