package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.UserCardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCardsRepository extends JpaRepository<UserCardsEntity, Long> {
}
