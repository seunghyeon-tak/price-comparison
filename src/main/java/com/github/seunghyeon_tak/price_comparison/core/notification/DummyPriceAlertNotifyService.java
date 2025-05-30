package com.github.seunghyeon_tak.price_comparison.core.notification;

import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DummyPriceAlertNotifyService implements PriceAlertNotifyService {
    @Override
    public void sendPriceDropAlert(UserEntity user, ProductEntity product, BigDecimal latestPrice) {
        System.out.println("[test 알림] " + user.getEmail() + "님, " + product.getName() + " 가격이 " + latestPrice + "원으로 떨어졌습니다.");
    }
}
