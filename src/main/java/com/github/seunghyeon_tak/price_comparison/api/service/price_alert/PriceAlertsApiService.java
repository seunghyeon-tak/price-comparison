package com.github.seunghyeon_tak.price_comparison.api.service.price_alert;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.price_alerts.PriceAlertsDto;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.price_alert.PriceAlertResponseCode;
import com.github.seunghyeon_tak.price_comparison.core.redis.RedisProductPriceCacheService;
import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.repository.price_alerts.PriceAlertsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceAlertsApiService {
    private final PriceAlertsRepository priceAlertsRepository;
    private final RedisProductPriceCacheService redisProductPriceCacheService;

    public void checkDuplicateAlert(UserEntity user, ProductEntity product, BigDecimal targetPrice) {
        priceAlertsRepository.findByUserAndProductAndTargetPriceAndIsActive(user, product, targetPrice, true)
                .ifPresent(alert -> {
                    throw new ApiException(PriceAlertResponseCode.DUPLICATE_PRODUCT);
                });
    }

    public void createAlert(PriceAlertsEntity priceAlerts) {
        priceAlertsRepository.save(priceAlerts);
    }

    public Optional<PriceAlertsEntity> alertProductCheck(UserEntity user, ProductEntity product) {
        return priceAlertsRepository.findByUserAndProductAndIsActive(user, product, true);
    }

    public void deactivateAlertUpdate(PriceAlertsEntity priceAlerts) {
        priceAlerts.setIsActive(false);
        priceAlertsRepository.save(priceAlerts);
    }

    public Page<PriceAlertsDto> findPriceAlertsWithLatestPrice(Pageable pageable, Long userId) {
        return priceAlertsRepository.findPriceAlertsWithLatestPrice(userId, pageable);
    }
}
