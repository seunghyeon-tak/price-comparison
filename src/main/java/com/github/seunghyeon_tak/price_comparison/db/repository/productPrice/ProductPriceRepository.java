package com.github.seunghyeon_tak.price_comparison.db.repository.productPrice;

import com.github.seunghyeon_tak.price_comparison.db.domain.ProductPriceEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ProductPriceRepository extends JpaRepository<ProductPriceEntity, Long>, ProductPriceRepositoryCustom {
    @Query("SELECT p.price FROM ProductPriceEntity p WHERE p.product.id = :productId ORDER BY p.crawledAt DESC LIMIT 1")
    BigDecimal findLatestPriceByProductId(@Param("productId") Long productId);
}
