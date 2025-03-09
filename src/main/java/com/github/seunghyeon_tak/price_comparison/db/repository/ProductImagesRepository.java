package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.ProductImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImagesRepository extends JpaRepository<ProductImagesEntity, Long> {
}
