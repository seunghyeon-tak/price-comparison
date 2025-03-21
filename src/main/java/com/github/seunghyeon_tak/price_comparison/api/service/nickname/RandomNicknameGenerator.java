package com.github.seunghyeon_tak.price_comparison.api.service.nickname;

import com.github.seunghyeon_tak.price_comparison.db.repository.adjectives.AdjectivesRepository;
import com.github.seunghyeon_tak.price_comparison.db.repository.nouns.NounsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomNicknameGenerator implements NicknameGenerator {
    private final AdjectivesRepository adjectivesRepository;
    private final NounsRepository nounsRepository;

    @Override
    public String generate() {
        String adj = adjectivesRepository.findRandomAdjective();
        String noun = nounsRepository.findRandomNoun();

        return adj + noun;
    }
}
