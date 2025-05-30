package com.github.seunghyeon_tak.price_comparison.scheduler;

import com.github.seunghyeon_tak.price_comparison.core.alert.AlertSender;
import com.github.seunghyeon_tak.price_comparison.core.notification.DummyPriceAlertNotifyService;
import com.github.seunghyeon_tak.price_comparison.core.scheduler.PriceAlertTriggerService;
import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.repository.price_alerts.PriceAlertsRepository;
import com.github.seunghyeon_tak.price_comparison.db.repository.productPrice.ProductPriceRepository;
import com.github.seunghyeon_tak.price_comparison.fixture.PriceAlertsFixture;
import com.github.seunghyeon_tak.price_comparison.fixture.ProductFixture;
import com.github.seunghyeon_tak.price_comparison.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceAlertTriggerServiceTest {
    @InjectMocks
    private PriceAlertTriggerService priceAlertTriggerService;

    @Mock
    private PriceAlertsRepository priceAlertsRepository;

    @Mock
    private ProductPriceRepository productPriceRepository;

    @Mock
    private AlertSender alertSender;

    @Mock
    private DummyPriceAlertNotifyService dummyPriceAlertNotifyService;

    @Test
    @DisplayName("가격이 하락해 알림이 정상적으로 전송되는 경우")
    void test1() {
        // given
        ProductEntity product = ProductFixture.create(1L, "에어팟 프로");
        UserEntity user = UserFixture.create(1L, "user@test.com");

        PriceAlertsEntity priceAlerts = PriceAlertsFixture.create(1L, new BigDecimal("150000"));
        priceAlerts.setProduct(product);
        priceAlerts.setUser(user);

        when(priceAlertsRepository.findByIsActiveTrue()).thenReturn(List.of(priceAlerts));
        when(productPriceRepository.findLatestPriceByProductId(product.getId()))
                .thenReturn(new BigDecimal("120000"));

        // when
        priceAlertTriggerService.processAlerts();

        // then
        verify(alertSender).send(user, product, new BigDecimal("120000"));
    }
}
