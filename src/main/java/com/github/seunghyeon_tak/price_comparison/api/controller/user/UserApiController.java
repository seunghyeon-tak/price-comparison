package com.github.seunghyeon_tak.price_comparison.api.controller.user;

import com.github.seunghyeon_tak.price_comparison.api.business.user.UserApiBusiness;
import com.github.seunghyeon_tak.price_comparison.api.service.user.dto.LoginInfo;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserLoginRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserInfoDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserLoginResponse;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController {
    private final UserApiBusiness userApiBusiness;

    @PostMapping("/public/signup")
    @ControllerLoggable("회원가입 컨트롤러")
    public Api<Void> signup(@RequestBody @Valid UserSignupRequest request) {
        userApiBusiness.signup(request);

        return Api.success();
    }

    @PostMapping("/public/login")
    @ControllerLoggable("로그인 컨트롤러")
    public Api<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request, HttpServletResponse response) {
        LoginInfo loginInfo = userApiBusiness.login(request);

        // HttpOnly Cookie
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", loginInfo.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return Api.success(UserLoginResponse.builder()
                .accessToken(loginInfo.getAccessToken())
                .user(UserInfoDto.builder()
                        .id(loginInfo.getUserId())
                        .email(loginInfo.getEmail())
                        .nickname(loginInfo.getNickname())
                        .alertType(loginInfo.getAlertType())
                        .build())
                .build());
    }

    @GetMapping("/me")
    public Api<Void> getUserInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        System.out.println(">>>>>>>>>>>> userId : " + userId);
        return Api.success();

    }
}
