package com.github.seunghyeon_tak.price_comparison.db.repository.nouns;

import com.github.seunghyeon_tak.price_comparison.db.domain.NounsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NounsRepository extends JpaRepository<NounsEntity, Long>, NounsRepositoryCustom {
}
