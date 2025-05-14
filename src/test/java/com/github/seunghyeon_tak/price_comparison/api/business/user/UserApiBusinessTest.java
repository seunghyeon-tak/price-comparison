package com.github.seunghyeon_tak.price_comparison.api.business.user;

import com.github.seunghyeon_tak.price_comparison.api.converter.user.UserApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.converter.user.UserFavoritesConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.store.StoreApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserFavoritesService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserPreferredStoreService;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserWishlistRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserFavoritesResponseCode;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserFavoritesEntity;
import com.github.seunghyeon_tak.price_comparison.fixture.ProductFixture;
import com.github.seunghyeon_tak.price_comparison.fixture.UserFavoritesFixture;
import com.github.seunghyeon_tak.price_comparison.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserApiBusinessTest {
    @InjectMocks
    private UserApiBusiness userApiBusiness;

    @Mock
    private UserApiConverter userApiConverter;

    @Mock
    private UserFavoritesConverter userFavoritesConverter;

    @Mock
    private UserApiService userApiService;

    @Mock
    private StoreApiService storeApiService;

    @Mock
    private UserPreferredStoreService userPreferredStoreService;

    @Mock
    private ProductApiService productApiService;

    @Mock
    private UserFavoritesService userFavoritesService;

    @Test
    @DisplayName("회원가입_성공")
    void test1() {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .email("test@test.com")
                .password("password1234")
                .build();
        UserEntity user = UserEntity.builder()
                .email("test@test.com")
                .password("password1234")
                .build();

//        given(userApiConverter.toEntity(request)).willReturn(user);
        doNothing().when(userApiService).duplicateEmail("test@test.com");
        when(userApiConverter.toEntity(any(UserSignupRequest.class))).thenReturn(user);
        doNothing().when(userApiService).save(user);

        // when
        userApiBusiness.signup(request);

        // then
        verify(userApiService).duplicateEmail(request.getEmail());
        verify(userApiConverter).toEntity(request);
        verify(userApiService).save(user);
    }

    @Test
    @DisplayName("회원가입_중복_이메일_예외")
    void test2() {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .email("duplicate@test.com")
                .password("password1234")
                .build();

        doThrow(new ApiException(UserResponseCode.USER_EMAIL_DUPLICATE))
                .when(userApiService).duplicateEmail("duplicate@test.com");

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            userApiBusiness.signup(request);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(UserResponseCode.USER_EMAIL_DUPLICATE.getDescription());

        verify(userApiService).duplicateEmail("duplicate@test.com");
    }

    @Test
    @DisplayName("선호_쇼핑몰_저장_성공")
    void test3() {
        // given
        Long userId = 1L;
        List<Long> storeIds = List.of(1L, 2L);

        UserEntity dummyUser = UserEntity.builder()
                .id(userId)
                .email("test@test.com")
                .build();

        when(userApiService.getUserId(userId)).thenReturn(dummyUser);
        doNothing().when(storeApiService).validateStoresExist(storeIds);
        doNothing().when(userPreferredStoreService).updatePreferredStores(userId, storeIds);

        // when
        userApiBusiness.preferredStores(userId, storeIds);

        // then
        verify(userApiService).getUserId(userId);
        verify(storeApiService).validateStoresExist(storeIds);
        verify(userPreferredStoreService).updatePreferredStores(userId, storeIds);
    }

    @Test
    @DisplayName("선호_쇼핑몰_저장_실패_유저없음")
    void test4() {
        // given
        Long userId = 999L;
        List<Long> storeIds = List.of(1L, 2L);

        // 예외 던지기
        when(userApiService.getUserId(userId))
                .thenThrow(new ApiException(UserResponseCode.USER_NOT_FOUND));

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            userApiBusiness.preferredStores(userId, storeIds);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(UserResponseCode.USER_NOT_FOUND.getDescription());

        verify(userApiService).getUserId(userId);
        verify(storeApiService, never()).validateStoresExist(any());
        verify(userPreferredStoreService, never()).updatePreferredStores(any(), any());
    }

    @Test
    @DisplayName("사용자_상품_찜_추가_성공")
    void test5() {
        // given
        Long userId = 1L;
        UserWishlistRequest request = UserWishlistRequest.builder()
                .productId(1L)
                .build();
        UserEntity user = UserFixture.create(1L, "test@test.com");
        ProductEntity product = ProductFixture.create(1L, "테스트 상품");
        UserFavoritesEntity userFavorites = UserFavoritesFixture.create(user, product);

        when(userApiService.getUserId(userId)).thenReturn(user);
        when(productApiService.getProduct(request.getProductId())).thenReturn(product);
        when(userFavoritesConverter.toEntity(user, product)).thenReturn(userFavorites);
        doNothing().when(userFavoritesService).save(userFavorites);

        // when
        userApiBusiness.addWishlist(userId, request.getProductId());

        // then
        verify(userApiService).getUserId(userId);
        verify(productApiService).getProduct(request.getProductId());
        verify(userFavoritesConverter).toEntity(user, product);
        verify(userFavoritesService).save(userFavorites);
    }

    @Test
    @DisplayName("사용자_상품_찜_존재하지않을때")
    void test6() {
        // given
        Long userId = 1L;
        UserWishlistRequest request = UserWishlistRequest.builder()
                .productId(999L)
                .build();

        // 이미 찜된 상품이 존재한다고 가정
        when(userFavoritesService.existsByUserAndProduct(userId, request.getProductId()))
                .thenReturn(true);

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            userApiBusiness.addWishlist(userId, request.getProductId());
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(UserFavoritesResponseCode.EXISTENCE_IN_PRODUCT.getDescription());

        verify(userApiService, never()).getUserId(userId);
        verify(productApiService, never()).getProduct(request.getProductId());
        verify(userFavoritesConverter, never()).toEntity(any(), any());
        verify(userFavoritesService, never()).save(any());
    }

    @Test
    @DisplayName("사용자_찜_제거_성공")
    void test7() {
        // given
        Long userId = 1L;
        Long productId = 2L;

        UserEntity user = UserFixture.create(userId, "test@test.com");
        ProductEntity product = ProductFixture.create(productId, "테스트 상품");

        when(userApiService.getUserId(userId)).thenReturn(user);
        when(productApiService.getProduct(productId)).thenReturn(product);
        doNothing().when(userFavoritesService).delete(userId, productId);

        // when
        userApiBusiness.removeWishlist(userId, productId);

        // then
        verify(userApiService).getUserId(userId);
        verify(productApiService).getProduct(productId);
        verify(userFavoritesService).delete(userId, productId);
    }

    @Test
    @DisplayName("사용자_찜_제거_실패")
    void test8() {
        // given
        Long userId = 1L;
        Long productId = 999L;

        UserEntity user = UserFixture.create(userId, "test@test.com");
        ProductEntity product = ProductFixture.create(productId, "없는 상품");

        when(userApiService.getUserId(userId)).thenReturn(user);
        when(productApiService.getProduct(productId)).thenReturn(product);

        // 예외 발생
        doThrow(new ApiException(UserFavoritesResponseCode.NOT_FOUND_IN_USER_FAVORITES))
                .when(userFavoritesService).delete(userId, productId);

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            userApiBusiness.removeWishlist(userId, productId);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(UserFavoritesResponseCode.NOT_FOUND_IN_USER_FAVORITES.getDescription());

        verify(userApiService).getUserId(userId);
        verify(productApiService).getProduct(productId);
        verify(userFavoritesService).delete(userId, productId);
    }

}