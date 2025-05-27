package com.github.seunghyeon_tak.price_comparison.core.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceAlertTriggerScheduler {
    private final PriceAlertTriggerService priceAlertTriggerService;

    @Scheduled(cron = "0 0 * * * *")  // 매 정시 실행
//    @Scheduled(fixedRate = 60000)  // 1분 - 테스트 용
//    @Scheduled(cron = "*/20 * * * * *") // 20초마다 (초기 디버깅용)
    public void run() {
        priceAlertTriggerService.processAlerts();
    }
}
