package com.github.seunghyeon_tak.price_comparison.api.converter.price_alert;

import com.github.seunghyeon_tak.price_comparison.common.annotation.Converter;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.price_alert.PriceAlertsRequest;
import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;

@Converter
public class PriceAlertsApiConverter {
    public PriceAlertsEntity toEntity(UserEntity user, ProductEntity product, PriceAlertsRequest request) {
        return PriceAlertsEntity.builder()
                .user(user)
                .product(product)
                .targetPrice(request.getTargetPrice())
                .isActive(true)
                .build();
    }
}
