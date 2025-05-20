package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface PriceAlertsRepository extends JpaRepository<PriceAlertsEntity, Long> {
    Optional<PriceAlertsEntity> findByUserAndProductAndTargetPriceAndIsActive(UserEntity user, ProductEntity product, BigDecimal targetPrice, boolean isActive);
}
