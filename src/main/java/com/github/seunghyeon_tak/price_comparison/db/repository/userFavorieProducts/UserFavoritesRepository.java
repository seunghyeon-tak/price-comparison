package com.github.seunghyeon_tak.price_comparison.db.repository.userFavorieProducts;

import com.github.seunghyeon_tak.price_comparison.db.domain.UserFavoritesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFavoritesRepository extends JpaRepository<UserFavoritesEntity, Long>, UserFavoritesRepositoryCustom {
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    Optional<UserFavoritesEntity> findByUserIdAndProductId(Long userId, Long productId);

    Page<UserFavoritesEntity> findByUserId(Long userId, Pageable pageable);
}
