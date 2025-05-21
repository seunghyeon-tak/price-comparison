package com.github.seunghyeon_tak.price_comparison.api.controller.price_alert;

import com.github.seunghyeon_tak.price_comparison.api.business.price_alert.PriceAlertsApiBusiness;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.price_alert.PriceAlertsRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/price-alerts")
@RequiredArgsConstructor
public class PriceAlertsApiController {
    private final PriceAlertsApiBusiness priceAlertsApiBusiness;

    private Long getCurrentUserId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    @PostMapping("")
    @ControllerLoggable("가격 알림 설정 등록 컨트롤러")
    public Api<Void> registerPriceAlert(@RequestBody PriceAlertsRequest request) {
        Long userId = getCurrentUserId();
        priceAlertsApiBusiness.registerPriceAlert(userId, request);
        return Api.success();
    }
}
