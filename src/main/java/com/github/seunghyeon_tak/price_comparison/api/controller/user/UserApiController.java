package com.github.seunghyeon_tak.price_comparison.api.controller.user;

import com.github.seunghyeon_tak.price_comparison.api.business.user.UserApiBusiness;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserPreferredStoresRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserWishlistRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserFavoritesProductDto;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController {
    private final UserApiBusiness userApiBusiness;

    private Long getCurrentUserId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

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

    @PostMapping("/wishlist")
    @ControllerLoggable("상품 찜 추가 컨트롤러")
    public Api<Void> addToWishlist(@RequestBody UserWishlistRequest request) {
        Long userId = getCurrentUserId();
        userApiBusiness.addWishlist(userId, request.getProductId());
        return Api.success();
    }

    @DeleteMapping("/wishlist/{productId}")
    @ControllerLoggable("상품 찜 삭제 컨트롤러")
    public Api<Void> removeFromWishlist(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        userApiBusiness.removeWishlist(userId, productId);
        return Api.success();
    }

    @GetMapping("/wishlist")
    @ControllerLoggable("상품 찜 리스트 컨트롤러")
    public Api<Page<UserFavoritesProductDto>> getUserWishlist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,DESC") String sort
    ) {
        String[] sortParams = sort.split(",");
        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0])
        );
        Long userId = getCurrentUserId();
        Page<UserFavoritesProductDto> response = userApiBusiness.getWishlist(pageable, userId);
        return Api.success(response);
    }
}
