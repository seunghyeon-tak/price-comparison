package com.github.seunghyeon_tak.price_comparison.api.business.auth;

import com.github.seunghyeon_tak.price_comparison.api.service.auth.AuthApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.dto.LoginInfo;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserLoginRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.auth.AuthResponseCode;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.enums.AlertType;
import com.github.seunghyeon_tak.price_comparison.external.kakao.KakaoOAuthService;
import com.github.seunghyeon_tak.price_comparison.external.kakao.dto.KakaoAccessTokenResponse;
import com.github.seunghyeon_tak.price_comparison.external.kakao.dto.KakaoUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthApiBusinessTest {
    @InjectMocks
    private AuthApiBusiness authApiBusiness;

    @Mock
    private AuthApiService authApiService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private KakaoOAuthService kakaoOAuthService;

    @Test
    @DisplayName("로그인_성공")
    void test1() {
        // given
        String rawPassword = "password1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserEntity user = UserEntity.builder()
                .email("test@test.com")
                .password(encodedPassword)
                .nickname("testuser1")
                .alertType(AlertType.NONE)
                .isActive(true)
                .build();

        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password1234")
                .build();

        LoginInfo mockLoginInfo = LoginInfo.builder()
                .accessToken("mock-access-token")
                .refreshToken("mock-refresh-token")
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .alertType(user.getAlertType())
                .build();

        when(authApiService.login(any(UserLoginRequest.class))).thenReturn(mockLoginInfo);

        // when
        LoginInfo result = authApiBusiness.login(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("mock-access-token");
        assertThat(result.getRefreshToken()).isEqualTo("mock-refresh-token");
        assertThat(result.getUserId()).isEqualTo(user.getId());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getNickname()).isEqualTo(user.getNickname());
        assertThat(result.getAlertType()).isEqualTo(user.getAlertType());

        verify(authApiService).login(request);
    }

    @Test
    @DisplayName("존재하지_않는_이메일로_로그인_요청시")
    void test2() {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password1234")
                .build();

        when(authApiService.login(any(UserLoginRequest.class)))
                .thenThrow(new ApiException(UserResponseCode.EMAIL_NOT_EXIST));

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            authApiBusiness.login(request);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(UserResponseCode.EMAIL_NOT_EXIST.getDescription());

        verify(authApiService).login(request);
    }

    @Test
    @DisplayName("비밀번호가_틀린_경우")
    void test3() {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password4321")
                .build();

        when(authApiService.login(any(UserLoginRequest.class)))
                .thenThrow(new ApiException(UserResponseCode.USER_PASSWORD_WRONG));

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            authApiBusiness.login(request);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(UserResponseCode.USER_PASSWORD_WRONG.getDescription());

        verify(authApiService).login(request);
    }

    @Test
    @DisplayName("Redis_저장_실패시")
    void test4() {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password1234")
                .build();

        when(authApiService.login(any(UserLoginRequest.class)))
                .thenThrow(new ApiException(AuthResponseCode.REDIS_SAVE_FAILED));

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            authApiBusiness.login(request);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(AuthResponseCode.REDIS_SAVE_FAILED.getDescription());

        verify(authApiService).login(request);

    }

    @Test
    @DisplayName("비활성화_된_사용자_로그인_시도")
    void test5() {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password1234")
                .build();

        when(authApiService.login(any(UserLoginRequest.class)))
                .thenThrow(new ApiException(UserResponseCode.USER_NOT_ACTIVE));

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            authApiBusiness.login(request);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(UserResponseCode.USER_NOT_ACTIVE.getDescription());

        verify(authApiService).login(request);
    }

    @Test
    @DisplayName("카카오_로그인_성공")
    void test6() {
        // given
        String code = "auth-code";
        String accessToken = "mock-access-token";

        KakaoAccessTokenResponse kakaoResponse = KakaoAccessTokenResponse.builder()
                .accessToken("mock-access-token")
                .refreshToken("mock-refresh-token")
                .build();

        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.builder()
                .id(123L)
                .kakaoAccount(new KakaoUserInfo.KakaoAccount("test@test.com", "nickname"))
                .build();

        LoginInfo mockLoginInfo = LoginInfo.builder()
                .accessToken("jwt-access-token")
                .refreshToken("jwt-refresh-token")
                .userId(1L)
                .email("test@test.com")
                .nickname("mock-nickname")
                .alertType(AlertType.NONE)
                .build();

        when(kakaoOAuthService.kakaoRequestAccessToken(code)).thenReturn(kakaoResponse);
        when(kakaoOAuthService.kakaoRequestUserInfo(accessToken)).thenReturn(kakaoUserInfo);
        when(authApiService.loginOrSignupByKakao("test@test.com")).thenReturn(mockLoginInfo);

        // when
        LoginInfo result = authApiBusiness.kakaoLogin(code);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getNickname()).isEqualTo("mock-nickname");

        verify(kakaoOAuthService).kakaoRequestAccessToken(code);
        verify(kakaoOAuthService).kakaoRequestUserInfo(accessToken);
        verify(authApiService).loginOrSignupByKakao("test@test.com");
    }

    @Test
    @DisplayName("카카오_로그인_토큰_발급_실패")
    void test7() {
        // given
        String code = "invalid-auth-code";

        when(kakaoOAuthService.kakaoRequestAccessToken(code))
                .thenThrow(new ApiException(AuthResponseCode.KAKAO_TOKEN_REQUEST_FAIL));

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            authApiBusiness.kakaoLogin(code);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(AuthResponseCode.KAKAO_TOKEN_REQUEST_FAIL.getDescription());

        verify(kakaoOAuthService).kakaoRequestAccessToken(code);
    }

    @Test
    @DisplayName("카카오_로그인_사용자_정보_요청_실패")
    void test8() {
        // give
        String code = "kakao-code";
        KakaoAccessTokenResponse kakaoResponse = KakaoAccessTokenResponse.builder()
                .accessToken("mock-access-token")
                .refreshToken("mock-refresh-token")
                .build();
        String accessToken = kakaoResponse.getAccessToken();

        when(kakaoOAuthService.kakaoRequestAccessToken(code)).thenReturn(kakaoResponse);
        when(kakaoOAuthService.kakaoRequestUserInfo(accessToken))
                .thenThrow(new ApiException(AuthResponseCode.KAKAO_USERINFO_REQUEST_FAIL));

        // when & then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            authApiBusiness.kakaoLogin(code);
        });

        assertThat(apiException.getErrorDescription())
                .isEqualTo(AuthResponseCode.KAKAO_USERINFO_REQUEST_FAIL.getDescription());

        verify(kakaoOAuthService).kakaoRequestUserInfo(accessToken);
    }
}