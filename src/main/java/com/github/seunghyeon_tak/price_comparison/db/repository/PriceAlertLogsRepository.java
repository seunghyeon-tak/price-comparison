package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertLogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceAlertLogsRepository extends JpaRepository<PriceAlertLogsEntity, Long> {
}
