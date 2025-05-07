package com.github.seunghyeon_tak.price_comparison.api.business.user;

import com.github.seunghyeon_tak.price_comparison.api.converter.user.UserApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.store.StoreApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserPreferredStoreService;
import com.github.seunghyeon_tak.price_comparison.common.annotation.Business;
import com.github.seunghyeon_tak.price_comparison.common.annotation.BusinessLoggable;
import com.github.seunghyeon_tak.price_comparison.common.annotation.LogException;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@RequiredArgsConstructor
public class UserApiBusiness {
    private final UserApiConverter userApiConverter;
    private final UserApiService userApiService;
    private final StoreApiService storeApiService;
    private final UserPreferredStoreService userPreferredStoreService;

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
}
