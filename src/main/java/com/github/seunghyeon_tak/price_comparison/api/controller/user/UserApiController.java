package com.github.seunghyeon_tak.price_comparison.api.controller.user;

import com.github.seunghyeon_tak.price_comparison.api.business.user.UserApiBusiness;
import com.github.seunghyeon_tak.price_comparison.api.service.user.dto.LoginInfo;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserLoginRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserInfoDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserLoginResponse;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import com.github.seunghyeon_tak.price_comparison.common.util.CookieUtils;
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

    @GetMapping("/me")
    public Api<Void> getUserInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        System.out.println(">>>>>>>>>>>> userId : " + userId);
        return Api.success();

    }
}
