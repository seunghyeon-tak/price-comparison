package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.UserFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFavoritesRepository extends JpaRepository<UserFavoritesEntity, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    Optional<UserFavoritesEntity> findByUserIdAndProductId(Long userId, Long productId);
}
