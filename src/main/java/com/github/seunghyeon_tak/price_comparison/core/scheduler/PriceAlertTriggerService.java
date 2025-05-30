package com.github.seunghyeon_tak.price_comparison.core.scheduler;

import com.github.seunghyeon_tak.price_comparison.core.alert.AlertSender;
import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.repository.price_alerts.PriceAlertsRepository;
import com.github.seunghyeon_tak.price_comparison.db.repository.productPrice.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceAlertTriggerService {
    private final PriceAlertsRepository priceAlertsRepository;
    private final ProductPriceRepository productPriceRepository;
    private final AlertSender alertSender;

    @Transactional
    public void processAlerts() {
        log.info("[PriceAlertTrigger] 스케줄러 실행 시작");
        List<PriceAlertsEntity> alerts = priceAlertsRepository.findByIsActiveTrue();
        log.info("[PriceAlertTrigger] 대상 알림 수 : {}", alerts.size());

        for (PriceAlertsEntity alert : alerts) {
            Long productId = alert.getProduct().getId();
            BigDecimal latestPrice = productPriceRepository.findLatestPriceByProductId(alert.getProduct().getId());

            if (latestPrice == null) {
                log.warn("[PriceAlertTrigger] 가격 없음 - productId : {}", productId);
                continue;
            }

            if (latestPrice.compareTo(alert.getTargetPrice()) <= 0) {
                alertSender.send(alert.getUser(), alert.getProduct(), latestPrice);

                alert.setIsActive(false);
                priceAlertsRepository.save(alert);

                log.info("[PriceAlertTrigger] 알림 발송 완료 - 상품 {}, 유저 {}", productId, alert.getUser().getId());
            } else {
                log.debug("[PriceAlertTrigger] 조건 미충족 - 현재가: {}, 목표가: {}", latestPrice, alert.getTargetPrice());
            }
        }

        log.info("[PriceAlertTrigger] 스케줄러 실행 완료");
    }
}
