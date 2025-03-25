package com.github.seunghyeon_tak.price_comparison.api.controller.user;

import com.github.seunghyeon_tak.price_comparison.api.business.user.UserApiBusiness;
import com.github.seunghyeon_tak.price_comparison.api.converter.user.UserApiConverter;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserLoginRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserLoginResponse;
import com.github.seunghyeon_tak.price_comparison.common.response.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Api<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        userApiBusiness.login(request);
        return Api.success(token);
    }
}
