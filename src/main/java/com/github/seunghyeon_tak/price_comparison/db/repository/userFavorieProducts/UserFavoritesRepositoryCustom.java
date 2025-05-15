package com.github.seunghyeon_tak.price_comparison.db.repository.userFavorieProducts;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserFavoritesProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserFavoritesRepositoryCustom {
    Page<UserFavoritesProductDto> findFavoritesByUserId(Long userId, Pageable pageable);
}
