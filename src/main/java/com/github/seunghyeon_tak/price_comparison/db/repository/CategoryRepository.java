package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
