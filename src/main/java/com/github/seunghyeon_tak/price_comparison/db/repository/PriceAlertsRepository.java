package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceAlertsRepository extends JpaRepository<PriceAlertsEntity, Long> {
}
