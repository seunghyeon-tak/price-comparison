package com.github.seunghyeon_tak.price_comparison.db.repository;

import com.github.seunghyeon_tak.price_comparison.db.domain.UserCardInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCardInformationRepository extends JpaRepository<UserCardInformationEntity, Long> {
}
