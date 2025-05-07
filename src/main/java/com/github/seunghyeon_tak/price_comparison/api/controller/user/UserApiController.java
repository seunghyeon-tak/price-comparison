package com.github.seunghyeon_tak.price_comparison.api.controller.user;

import com.github.seunghyeon_tak.price_comparison.api.business.user.UserApiBusiness;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserPreferredStoresRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{userId}/preferred-stores")
    @ControllerLoggable("쇼핑몰 선택 컨트롤러")
    public Api<Void> preferredStores(@PathVariable Long userId, @RequestBody UserPreferredStoresRequest request) {
        userApiBusiness.preferredStores(userId, request.getStoreIds());
        return Api.success();
    }

}
