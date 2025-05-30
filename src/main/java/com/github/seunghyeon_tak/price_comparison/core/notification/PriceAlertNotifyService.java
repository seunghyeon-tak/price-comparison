package com.github.seunghyeon_tak.price_comparison.core.notification;

import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;

import java.math.BigDecimal;

public interface PriceAlertNotifyService {
    void sendPriceDropAlert(UserEntity user, ProductEntity product, BigDecimal latestPrice);
}
