package com.github.seunghyeon_tak.price_comparison.db.repository.price_alerts;

import com.github.seunghyeon_tak.price_comparison.db.domain.PriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface PriceAlertsRepository extends JpaRepository<PriceAlertsEntity, Long>, PriceAlertsRepositoryCustom {
    Optional<PriceAlertsEntity> findByUserAndProductAndTargetPriceAndIsActive(UserEntity user, ProductEntity product, BigDecimal targetPrice, boolean isActive);

    Optional<PriceAlertsEntity> findByUserAndProductAndIsActive(UserEntity user, ProductEntity product, boolean isActive);

    Page<PriceAlertsEntity> findByUserAndIsActiveTrue(UserEntity user, Pageable pageable);
}
