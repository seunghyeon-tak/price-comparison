package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.UserPreferredStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferredStoreRepository extends JpaRepository<UserPreferredStoreEntity, Long> {
    void deleteByUserId(Long userId);
}
