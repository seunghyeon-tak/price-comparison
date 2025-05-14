package com.github.seunghyeon_tak.price_comparison.api.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.seunghyeon_tak.price_comparison.api.business.user.UserApiBusiness;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserPreferredStoresRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserWishlistRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.product.ProductResponseCode;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.store.StoreResponseCode;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserFavoritesResponseCode;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserApiBusiness userApiBusiness;

    @Autowired
    private ObjectMapper objectMapper;

    private void userSecurityContext(Long userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(String.valueOf(userId), null);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("회원가입_API_호출_성공")
    void test1() throws Exception {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .email("test@test.com")
                .password("password1234")
                .build();

        doNothing().when(userApiBusiness).signup(request);

        // when & when
        mockMvc.perform(post("/api/v1/user/public/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultCode").value(200))
                .andExpect(jsonPath("$.result.resultMessage").value("success"))
                .andExpect(jsonPath("$.result.resultDescription").value("success"));


        verify(userApiBusiness).signup(any());
    }

    @Test
    @DisplayName("회원가입_API_호출_이메일_중복")
    void test2() throws Exception {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .email("duplicate@test.com")
                .password("password1234")
                .build();

        doThrow(new ApiException(UserResponseCode.USER_EMAIL_DUPLICATE))
                .when(userApiBusiness).signup(any(UserSignupRequest.class));

        // when & then
        mockMvc.perform(post("/api/v1/user/public/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.resultCode").value(1000))
                .andExpect(jsonPath("$.result.resultMessage").value("중복된 이메일 입니다."))
                .andExpect(jsonPath("$.result.resultDescription").value("중복된 이메일 입니다."));
    }

    @Test
    @DisplayName("선호_쇼핑몰_저장_API_호출_성공")
    void test3() throws Exception {
        // given
        Long userId = 1L;
        UserPreferredStoresRequest request = UserPreferredStoresRequest.builder()
                .storeIds(List.of(1L, 2L))
                .build();

        doNothing().when(userApiBusiness).preferredStores(userId, request.getStoreIds());

        // when & then
        mockMvc.perform(post("/api/v1/user/{userId}/preferred-stores", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultCode").value(200))
                .andExpect(jsonPath("$.result.resultMessage").value("success"))
                .andExpect(jsonPath("$.result.resultDescription").value("success"));
    }

    @Test
    @DisplayName("선호_쇼핑몰_저장_API_호출_존재하지_않는_스토어")
    void test4() throws Exception {
        // given
        Long userId = 1L;
        UserPreferredStoresRequest request = UserPreferredStoresRequest.builder()
                .storeIds(List.of(999L))
                .build();

        doThrow(new ApiException(StoreResponseCode.STORE_IS_EMPTY))
                .when(userApiBusiness).preferredStores(userId, request.getStoreIds());

        // when & then
        mockMvc.perform(post("/api/v1/user/{userId}/preferred-stores", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.resultCode").value(4000))
                .andExpect(jsonPath("$.result.resultMessage").value("존재하지 않는 storeId 입니다."))
                .andExpect(jsonPath("$.result.resultDescription").value("존재하지 않는 storeId 입니다."));
    }

    @Test
    @DisplayName("사용자_상품_찜_추가")
    void test5() throws Exception {
        // given
        Long userId = 1L;
        userSecurityContext(userId);
        UserWishlistRequest request = UserWishlistRequest.builder()
                .productId(1L)
                .build();

        doNothing().when(userApiBusiness).addWishlist(userId, request.getProductId());

        // when & then
        mockMvc.perform(post("/api/v1/user/wishlist")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultCode").value(200))
                .andExpect(jsonPath("$.result.resultMessage").value("success"))
                .andExpect(jsonPath("$.result.resultDescription").value("success"));
    }

    @Test
    @DisplayName("사용자_상품_찜_이미_추가되어있을때")
    void test6() throws Exception {
        // given
        Long userId = 1L;
        userSecurityContext(userId);
        UserWishlistRequest request = UserWishlistRequest.builder()
                .productId(1L)
                .build();

        doThrow(new ApiException(UserFavoritesResponseCode.EXISTENCE_IN_PRODUCT))
                .when(userApiBusiness).addWishlist(userId, request.getProductId());

        // when & then
        mockMvc.perform(post("/api/v1/user/wishlist")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.resultCode").value(5000))
                .andExpect(jsonPath("$.result.resultMessage").value("이미 찜한 상품입니다."))
                .andExpect(jsonPath("$.result.resultDescription").value("이미 찜한 상품입니다."));
    }

    @Test
    @DisplayName("사용자_상품_찜_찾는_상품이_없을때")
    void test7() throws Exception {
        // given
        Long userId = 1L;
        userSecurityContext(userId);
        UserWishlistRequest request = UserWishlistRequest.builder()
                .productId(1L)
                .build();

        doThrow(new ApiException(ProductResponseCode.PRODUCT_NOT_FOUND))
                .when(userApiBusiness).addWishlist(userId, request.getProductId());

        // when & then
        mockMvc.perform(post("/api/v1/user/wishlist")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.resultCode").value(3000))
                .andExpect(jsonPath("$.result.resultMessage").value("찾는 상품이 없습니다."))
                .andExpect(jsonPath("$.result.resultDescription").value("찾는 상품이 없습니다."));
    }

    @Test
    @DisplayName("사용자_상품_찜_삭제_성공")
    void test8() throws Exception {
        // given
        userSecurityContext(1L);

        // when & then
        mockMvc.perform(delete("/api/v1/user/wishlist/{productId}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultCode").value("200"))
                .andExpect(jsonPath("$.result.resultMessage").value("success"))
                .andExpect(jsonPath("$.result.resultDescription").value("success"));
    }

    @Test
    @DisplayName("사용자_상품_찜_존재하지_않는_상품일때")
    void test9() throws Exception {
        // given
        userSecurityContext(1L);
        Long productId = 999L;

        doThrow(new ApiException(UserFavoritesResponseCode.NOT_FOUND_IN_USER_FAVORITES))
                .when(userApiBusiness).removeWishlist(1L, productId);

        // when & then
        mockMvc.perform(delete("/api/v1/user/wishlist/{productId}", productId)
                        .contentType("application/json"))
                .andExpect(jsonPath("$.result.resultCode").value(5001))
                .andExpect(jsonPath("$.result.resultMessage").value("사용자 찜 목록에서 찾을 수 없습니다."))
                .andExpect(jsonPath("$.result.resultDescription").value("사용자 찜 목록에서 찾을 수 없습니다."));
    }
}