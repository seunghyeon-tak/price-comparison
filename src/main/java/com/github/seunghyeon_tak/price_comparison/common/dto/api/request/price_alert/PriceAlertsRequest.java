package com.github.seunghyeon_tak.price_comparison.common.dto.api.request.price_alert;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceAlertsRequest {
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @NotNull(message = "목표 가격은 필수입니다.")
    @Min(value = 1, message = "목표 가격은 1원 이상이어야 합니다.")
    private BigDecimal targetPrice;
}
