package com.github.seunghyeon_tak.price_comparison.db.repository.price_alerts;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.price_alerts.PriceAlertsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PriceAlertsRepositoryCustom {
    Page<PriceAlertsDto> findPriceAlertsWithLatestPrice(Long userId, Pageable pageable);
}
