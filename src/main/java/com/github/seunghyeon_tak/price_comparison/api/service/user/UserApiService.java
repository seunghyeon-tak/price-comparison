package com.github.seunghyeon_tak.price_comparison.api.service.user;

import com.github.seunghyeon_tak.price_comparison.api.service.nickname.NicknameGenerator;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserLoginRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.response.enums.user.UserResponseCode;
import com.github.seunghyeon_tak.price_comparison.common.security.jwt.JwtProvider;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.enums.AlertType;
import com.github.seunghyeon_tak.price_comparison.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApiService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NicknameGenerator nicknameGenerator;
    private final JwtProvider jwtProvider;

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

    public String login(UserLoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(UserResponseCode.EMAIL_NOT_EXIST));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(UserResponseCode.USER_PASSWORD_WRONG);
        }

        jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        jwtProvider.generateRefreshToken(user.getId(), user.getEmail());
    }

}
