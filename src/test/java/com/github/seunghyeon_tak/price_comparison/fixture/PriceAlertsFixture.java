package com.github.seunghyeon_tak.price_comparison.fixture;

import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;

import java.math.BigDecimal;

public class PriceAlertsFixture {
    public static PriceAlertsEntity create(Long id, BigDecimal targetPrice) {
        return PriceAlertsEntity.builder()
                .id(id)
                .targetPrice(targetPrice)
                .build();
    }

}
