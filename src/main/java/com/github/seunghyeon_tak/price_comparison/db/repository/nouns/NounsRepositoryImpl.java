package com.github.seunghyeon_tak.price_comparison.db.repository.nouns;

import com.github.seunghyeon_tak.price_comparison.db.domain.QNounsEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class NounsRepositoryImpl implements NounsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public String findRandomNoun() {
        QNounsEntity nounsEntity = QNounsEntity.nounsEntity;

        Long totalCount = queryFactory
                .select(nounsEntity.count())
                .from(nounsEntity)
                .fetchOne();

        if (totalCount == null || totalCount == 0) {
            return null;
        }

        int randomOffset = ThreadLocalRandom.current().nextInt(totalCount.intValue());

        return queryFactory
                .select(nounsEntity.word)
                .from(nounsEntity)
                .offset(randomOffset)
                .limit(1)
                .fetchOne();
    }
}
