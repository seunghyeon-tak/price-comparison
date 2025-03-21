package com.github.seunghyeon_tak.price_comparison.db.repository.adjectives;

import com.github.seunghyeon_tak.price_comparison.db.domain.QAdjectivesEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class AdjectivesRepositoryImpl implements AdjectivesRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public String findRandomAdjective() {
        QAdjectivesEntity adjectivesEntity = QAdjectivesEntity.adjectivesEntity;

        Long totalCount = queryFactory
                .select(adjectivesEntity.count())
                .from(adjectivesEntity)
                .fetchOne();

        if (totalCount == null || totalCount == 0) {
            return null;
        }

        // 무작위 offset 생성
        int randomOffset = ThreadLocalRandom.current().nextInt(totalCount.intValue());

        return queryFactory
                .select(adjectivesEntity.word)
                .from(adjectivesEntity)
                .offset(randomOffset)
                .limit(1)
                .fetchOne();
    }
}
