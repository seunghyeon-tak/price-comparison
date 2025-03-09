package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.UserFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoritesRepository extends JpaRepository<UserFavoritesEntity, Long> {
}
