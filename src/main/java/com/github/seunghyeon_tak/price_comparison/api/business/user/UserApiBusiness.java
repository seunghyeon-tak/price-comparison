package com.github.seunghyeon_tak.price_comparison.api.business.user;

import com.github.seunghyeon_tak.price_comparison.api.converter.user.UserApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserApiService;
import com.github.seunghyeon_tak.price_comparison.common.annotation.Business;
import com.github.seunghyeon_tak.price_comparison.common.annotation.BusinessLoggable;
import com.github.seunghyeon_tak.price_comparison.common.annotation.LogException;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class UserApiBusiness {
    private final UserApiConverter userApiConverter;
    private final UserApiService userApiService;

    @Transactional
    @BusinessLoggable("회원가입 비지니스")
    @LogException
    public void signup(UserSignupRequest request) {
        userApiService.duplicateEmail(request.getEmail());
        UserEntity user = userApiConverter.toEntity(request);
        userApiService.save(user);
    }
}
