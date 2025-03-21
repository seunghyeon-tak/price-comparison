package com.github.seunghyeon_tak.price_comparison.db.repository.adjectives;

import com.github.seunghyeon_tak.price_comparison.db.domain.AdjectivesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdjectivesRepository extends JpaRepository<AdjectivesEntity, Long>, AdjectivesRepositoryCustom {

}
