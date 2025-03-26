package com.github.seunghyeon_tak.price_comparison.api.service.user;

import com.github.seunghyeon_tak.price_comparison.api.service.auth.RefreshTokenService;
import com.github.seunghyeon_tak.price_comparison.api.service.nickname.NicknameGenerator;
import com.github.seunghyeon_tak.price_comparison.api.service.user.dto.LoginInfo;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserResponseCode;
import com.github.seunghyeon_tak.price_comparison.common.security.jwt.JwtProvider;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.enums.AlertType;
import com.github.seunghyeon_tak.price_comparison.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserApiService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NicknameGenerator nicknameGenerator;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public UserEntity getUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserResponseCode.USER_NOT_FOUND));
    }

    public UserEntity getUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(UserResponseCode.EMAIL_NOT_EXIST));
    }

    public UserEntity signupByKakao(String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        return userOpt.orElseGet(() -> {
            UserEntity newUser = UserEntity.builder()
                    .email(email)
                    .password(UUID.randomUUID().toString())  // 임시 비번
                    .nickname(nicknameGenerator.generate())
                    .alertType(AlertType.NONE)
                    .build();
            return userRepository.save(newUser);
        });
    }

    public void passwordValid(String requestPassword, String userPassword) {
        if (!passwordEncoder.matches(requestPassword, userPassword)) {
            throw new ApiException(UserResponseCode.USER_PASSWORD_WRONG);
        }
    }

    public void duplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ApiException(UserResponseCode.USER_EMAIL_DUPLICATE);
        }
    }

    public void save(UserEntity userEntity) {
        if (userEntity == null) {
            throw new ApiException(UserResponseCode.USER_NULL_POINT);
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setNickname(nicknameGenerator.generate());
        userEntity.setAlertType(AlertType.NONE);
        userRepository.save(userEntity);
    }

}
