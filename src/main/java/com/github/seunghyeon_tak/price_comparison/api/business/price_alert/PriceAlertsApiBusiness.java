package com.github.seunghyeon_tak.price_comparison.api.business.price_alert;

import com.github.seunghyeon_tak.price_comparison.api.converter.price_alert.PriceAlertsApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.price_alert.PriceAlertsApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserApiService;
import com.github.seunghyeon_tak.price_comparison.common.annotation.Business;
import com.github.seunghyeon_tak.price_comparison.common.annotation.BusinessLoggable;
import com.github.seunghyeon_tak.price_comparison.common.annotation.LogException;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.price_alert.PriceAlertsRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.price_alert.PriceAlertResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Business
@RequiredArgsConstructor
public class PriceAlertsApiBusiness {
    private final PriceAlertsApiService priceAlertsApiService;
    private final ProductApiService productApiService;
    private final UserApiService userApiService;
    private final PriceAlertsApiConverter priceAlertsApiConverter;

    @BusinessLoggable("가격 알림 설정 등록 비지니스")
    @LogException
    public void registerPriceAlert(Long userId, PriceAlertsRequest request) {
        // 사용자 확인
        UserEntity user = userApiService.getUserId(userId);

        // 상품 존재 여부 확인 (존재하지 않으면 예외 throw)
        ProductEntity product = productApiService.getProduct(request.getProductId());

        // 중복 알림 여부 확인 (같은 상품 + 같은 가격에 이미 등록된 알림이 있는지)
        priceAlertsApiService.checkDuplicateAlert(user, product, request.getTargetPrice());

        // PriceAlertEntity로 변환
        PriceAlertsEntity priceAlertsEntity = priceAlertsApiConverter.toEntity(user, product, request);

        // 알림 저장
        priceAlertsApiService.createAlert(priceAlertsEntity);
    }

    @BusinessLoggable("가격 알림 설정 제거 비지니스")
    @LogException
    public void deactivatePriceAlert(Long userId, Long productId) {
        UserEntity user = userApiService.getUserId(userId);
        ProductEntity product = productApiService.getProduct(productId);
        PriceAlertsEntity priceAlertsEntity = priceAlertsApiService.alertProductCheck(user, product)
                .orElseThrow(() -> new ApiException(PriceAlertResponseCode.ALTER_NOT_FOUND));
        priceAlertsApiService.deactivateAlertUpdate(priceAlertsEntity);
    }
}
