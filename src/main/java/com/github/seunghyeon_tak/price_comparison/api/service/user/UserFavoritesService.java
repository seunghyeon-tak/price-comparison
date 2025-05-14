package com.github.seunghyeon_tak.price_comparison.api.service.user;

import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserFavoritesResponseCode;
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

    public void delete(Long userId, Long productId) {
        UserFavoritesEntity userFavoritesEntity = userFavoritesRepository
                .findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ApiException(UserFavoritesResponseCode.NOT_FOUND_IN_USER_FAVORITES));

        userFavoritesRepository.delete(userFavoritesEntity);
    }
}
