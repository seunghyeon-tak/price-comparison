package com.github.seunghyeon_tak.price_comparison.api.business.price_alert;

import com.github.seunghyeon_tak.price_comparison.api.converter.price_alert.PriceAlertsApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.price_alert.PriceAlertsApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserApiService;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.price_alert.PriceAlertsRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.price_alert.PriceAlertResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceAlertsApiBusinessTest {
    @InjectMocks
    private PriceAlertsApiBusiness priceAlertsApiBusiness;

    @Mock
    private PriceAlertsApiConverter priceAlertsApiConverter;

    @Mock
    private PriceAlertsApiService priceAlertsApiService;

    @Mock
    private UserApiService userApiService;

    @Mock
    private ProductApiService productApiService;

    @Test
    @DisplayName("상품 가격 알림 등록 성공")
    void test1() {
        // given
        Long userId = 1L;
        Long productId = 2L;
        PriceAlertsRequest request = PriceAlertsRequest.builder()
                .productId(productId)
                .targetPrice(BigDecimal.valueOf(10000))
                .build();
        UserEntity user = UserFixture.create(userId, "test@test.com");
        ProductEntity product = ProductFixture.create(productId, "테스트 상품");
        PriceAlertsEntity priceAlertsEntity = PriceAlertsFixture.create(3L, BigDecimal.valueOf(10000));

        when(userApiService.getUserId(userId)).thenReturn(user);
        when(productApiService.getProduct(productId)).thenReturn(product);
        doNothing().when(priceAlertsApiService).checkDuplicateAlert(user, product, request.getTargetPrice());
        when(priceAlertsApiConverter.toEntity(user, product, request)).thenReturn(priceAlertsEntity);
        doNothing().when(priceAlertsApiService).createAlert(priceAlertsEntity);

        // when
        priceAlertsApiBusiness.registerPriceAlert(userId, request);

        // then
        verify(userApiService).getUserId(userId);
        verify(productApiService).getProduct(productId);
        verify(priceAlertsApiService).checkDuplicateAlert(user, product, request.getTargetPrice());
        verify(priceAlertsApiConverter).toEntity(user, product, request);
        verify(priceAlertsApiService).createAlert(priceAlertsEntity);
    }

    @Test
    @DisplayName("상품 가격 알림 이미 같은 가격으로 등록되어있을때")
    void test2() {
        // given
        Long userId = 1L;
        Long productId = 2L;
        PriceAlertsRequest request = PriceAlertsRequest.builder()
                .productId(productId)
                .targetPrice(BigDecimal.valueOf(10000))
                .build();
        UserEntity user = UserFixture.create(userId, "test@test.com");
        ProductEntity product = ProductFixture.create(productId, "테스트 상품");

        when(userApiService.getUserId(userId)).thenReturn(user);
        when(productApiService.getProduct(productId)).thenReturn(product);
        doThrow(new ApiException(PriceAlertResponseCode.DUPLICATE_PRODUCT))
                .when(priceAlertsApiService).checkDuplicateAlert(user, product, BigDecimal.valueOf(10000));

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            priceAlertsApiBusiness.registerPriceAlert(userId, request);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(PriceAlertResponseCode.DUPLICATE_PRODUCT.getDescription());

        verify(priceAlertsApiService).checkDuplicateAlert(user, product, request.getTargetPrice());
        verify(priceAlertsApiConverter, never()).toEntity(any(), any(), any());
        verify(priceAlertsApiService, never()).createAlert(any());
    }

}