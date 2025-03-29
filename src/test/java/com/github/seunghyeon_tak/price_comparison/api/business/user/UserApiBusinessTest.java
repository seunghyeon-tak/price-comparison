package com.github.seunghyeon_tak.price_comparison.api.business.user;

import com.github.seunghyeon_tak.price_comparison.api.converter.user.UserApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserApiService;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private UserApiService userApiService;

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

}