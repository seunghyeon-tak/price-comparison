package com.github.seunghyeon_tak.price_comparison.api.service.price_alert;

import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.price_alert.PriceAlertResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.repository.PriceAlertsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PriceAlertsApiService {
    private final PriceAlertsRepository priceAlertsRepository;

    public void checkDuplicateAlert(UserEntity user, ProductEntity product, BigDecimal targetPrice) {
        priceAlertsRepository.findByUserAndProductAndTargetPriceAndIsActive(user, product, targetPrice, true)
                .ifPresent(alert -> {
                    throw new ApiException(PriceAlertResponseCode.DUPLICATE_PRODUCT);
                });
    }

    public void createAlert(PriceAlertsEntity priceAlerts) {
        priceAlertsRepository.save(priceAlerts);
    }
}
