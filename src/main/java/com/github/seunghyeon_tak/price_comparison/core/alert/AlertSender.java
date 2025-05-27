package com.github.seunghyeon_tak.price_comparison.core.alert;

import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AlertSender {
    public void send(UserEntity user, ProductEntity product, BigDecimal currentPrice) {
        System.out.printf("[ALERT] %s님, '%s'의 가격이 %s원으로 떨어졌습니다.! %n", user.getEmail(), product.getName(), currentPrice);
    }
}
