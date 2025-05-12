package com.github.seunghyeon_tak.price_comparison.api.service.user;

import com.github.seunghyeon_tak.price_comparison.db.domain.UserFavoritesEntity;
import com.github.seunghyeon_tak.price_comparison.db.repository.UserFavoritesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFavoritesService {
    private final UserFavoritesRepository userFavoritesRepository;

    public void save(UserFavoritesEntity userFavoritesEntity) {
        userFavoritesRepository.save(userFavoritesEntity);
    }

    public boolean existsByUserAndProduct(Long userId, Long productId) {
        return userFavoritesRepository.existsByUserIdAndProductId(userId, productId);
    }
}
