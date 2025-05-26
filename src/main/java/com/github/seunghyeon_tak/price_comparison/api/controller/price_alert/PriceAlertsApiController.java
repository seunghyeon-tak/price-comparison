package com.github.seunghyeon_tak.price_comparison.api.controller.price_alert;

import com.github.seunghyeon_tak.price_comparison.api.business.price_alert.PriceAlertsApiBusiness;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.price_alert.PriceAlertsRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.price_alerts.PriceAlertsDto;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/price-alerts")
@RequiredArgsConstructor
public class PriceAlertsApiController {
    private final PriceAlertsApiBusiness priceAlertsApiBusiness;

    private Long getCurrentUserId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    @PostMapping
    @ControllerLoggable("가격 알림 설정 등록 컨트롤러")
    public Api<Void> registerPriceAlert(@RequestBody PriceAlertsRequest request) {
        Long userId = getCurrentUserId();
        priceAlertsApiBusiness.registerPriceAlert(userId, request);
        return Api.success();
    }

    @PatchMapping("/{productId}")
    @ControllerLoggable("가격 알림 설정 제거 컨트롤러")
    public Api<Void> deactivatePriceAlert(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        priceAlertsApiBusiness.deactivatePriceAlert(userId, productId);
        return Api.success();
    }

    @GetMapping
    @ControllerLoggable("가격 알림 목록 컨트롤러")
    public Api<Page<PriceAlertsDto>> getActivePriceAlerts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,DESC") String sort
    ) {
        Long userId = getCurrentUserId();
        String[] sortParams = sort.split(",");
        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0])
        );

        Page<PriceAlertsDto> response = priceAlertsApiBusiness.getPriceAlerts(pageable, userId);

        return Api.success(response);
    }
}
