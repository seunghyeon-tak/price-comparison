package com.github.seunghyeon_tak.price_comparison.api.business.user;

import com.github.seunghyeon_tak.price_comparison.api.converter.user.UserApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.converter.user.UserFavoritesConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.store.StoreApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserFavoritesService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserPreferredStoreService;
import com.github.seunghyeon_tak.price_comparison.common.annotation.Business;
import com.github.seunghyeon_tak.price_comparison.common.annotation.BusinessLoggable;
import com.github.seunghyeon_tak.price_comparison.common.annotation.LogException;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserFavoritesProductDto;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserFavoritesResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserFavoritesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@RequiredArgsConstructor
public class UserApiBusiness {
    private final UserApiConverter userApiConverter;
    private final UserFavoritesConverter userFavoritesConverter;

    private final UserApiService userApiService;
    private final StoreApiService storeApiService;
    private final UserPreferredStoreService userPreferredStoreService;
    private final UserFavoritesService userFavoritesService;
    private final ProductApiService productApiService;

    @Transactional
    @BusinessLoggable("회원가입 비지니스")
    @LogException
    public void signup(UserSignupRequest request) {
        userApiService.duplicateEmail(request.getEmail());
        UserEntity user = userApiConverter.toEntity(request);
        userApiService.save(user);
    }

    @BusinessLoggable("쇼핑몰 선택 비지니스")
    @LogException
    public void preferredStores(Long userId, List<Long> storeIds) {
        userApiService.getUserId(userId);
        storeApiService.validateStoresExist(storeIds);
        userPreferredStoreService.updatePreferredStores(userId, storeIds);
    }

    @BusinessLoggable("사용자 찜 목록 추가 비지니스")
    @LogException
    public void addWishlist(Long userId, Long productId) {
        if (userFavoritesService.existsByUserAndProduct(userId, productId)) {
            throw new ApiException(UserFavoritesResponseCode.EXISTENCE_IN_PRODUCT);
        }

        UserEntity user = userApiService.getUserId(userId);
        ProductEntity product = productApiService.getProduct(productId);
        UserFavoritesEntity entity = userFavoritesConverter.toEntity(user, product);
        userFavoritesService.save(entity);
    }

    @BusinessLoggable("사용자 찜 목록 삭제 비지니스")
    @LogException
    public void removeWishlist(Long userId, Long productId) {
        userApiService.getUserId(userId);
        productApiService.getProduct(productId);
        userFavoritesService.delete(userId, productId);
    }

    @BusinessLoggable("사용자 찜 리스트 비지니스")
    @LogException
    public Page<UserFavoritesProductDto> getWishlist(Pageable pageable, Long userId) {
        return userFavoritesService.getFavoritesProducts(userId, pageable);
    }
}
