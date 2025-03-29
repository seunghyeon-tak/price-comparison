package com.github.seunghyeon_tak.price_comparison.api.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.seunghyeon_tak.price_comparison.api.business.auth.AuthApiBusiness;
import com.github.seunghyeon_tak.price_comparison.api.service.user.dto.LoginInfo;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserLoginRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user.UserResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.enums.AlertType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthApiBusiness authApiBusiness;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인_성공")
    void test1() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password1234")
                .build();

        LoginInfo loginInfo = LoginInfo.builder()
                .email("test@test.com")
                .nickname("testuser1")
                .accessToken("mock-access-token")
                .refreshToken("mock-refresh-token")
                .alertType(AlertType.NONE)
                .build();

        when(authApiBusiness.login(request)).thenReturn(loginInfo);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultCode").value(200))
                .andExpect(jsonPath("$.result.resultMessage").value("success"))
                .andExpect(jsonPath("$.result.resultDescription").value("success"))
                .andExpect(jsonPath("$.data.accessToken").value("mock-access-token"))
                .andExpect(jsonPath("$.data.user.email").value("test@test.com"))
                .andExpect(jsonPath("$.data.user.nickname").value("testuser1"));
    }

    @Test
    @DisplayName("잘못된_비밀번호")
    void test2() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password4321")
                .build();

        when(authApiBusiness.login(any(UserLoginRequest.class)))
                .thenThrow(new ApiException(UserResponseCode.USER_PASSWORD_WRONG));

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result.resultCode").value(1003))
                .andExpect(jsonPath("$.result.resultMessage").value("비밀번호가 틀렸습니다."))
                .andExpect(jsonPath("$.result.resultDescription").value("비밀번호가 틀렸습니다."));
    }

    @Test
    @DisplayName("존재하지_않는_이메일")
    void test3() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password4321")
                .build();

        when(authApiBusiness.login(request))
                .thenThrow(new ApiException(UserResponseCode.EMAIL_NOT_EXIST));

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.resultCode").value(1002))
                .andExpect(jsonPath("$.result.resultMessage").value("존재하지 않는 이메일입니다."))
                .andExpect(jsonPath("$.result.resultDescription").value("존재하지 않는 이메일입니다."));
    }

    @Test
    @DisplayName("비활성화_사용자")
    void test4() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password4321")
                .build();

        when(authApiBusiness.login(request))
                .thenThrow(new ApiException(UserResponseCode.USER_NOT_ACTIVE));

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.resultCode").value(1006))
                .andExpect(jsonPath("$.result.resultMessage").value("사용자가 비활성화 상태입니다."))
                .andExpect(jsonPath("$.result.resultDescription").value("사용자가 비활성화 상태입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("잘못된_json_요청_형식")
    void test5() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("")
                .password("")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.resultCode").value(404))
                .andExpect(jsonPath("$.result.resultMessage").value("잘못된 요청입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("refreshToken_쿠키_저장_확인")
    void test6() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@test.com")
                .password("password1234")
                .build();

        LoginInfo loginInfo = LoginInfo.builder()
                .email("test@test.com")
                .nickname("testuser1")
                .accessToken("mock-access-token")
                .refreshToken("mock-refresh-token")
                .alertType(AlertType.NONE)
                .build();

        when(authApiBusiness.login(any(UserLoginRequest.class)))
                .thenReturn(loginInfo);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(header().string("Set-Cookie", containsString("refreshToken=")))
                .andExpect(header().string("Set-Cookie", containsString("HttpOnly")))
                .andDo(print());
    }

    @Test
    @DisplayName("카카오_로그인_성공")
    void test7() throws Exception {
        // given
        String code = "mock-kakao-code";

        LoginInfo loginInfo = LoginInfo.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .userId(1L)
                .email("kakao@test.com")
                .nickname("kakaoUser")
                .alertType(AlertType.NONE)
                .build();

        when(authApiBusiness.kakaoLogin(code)).thenReturn(loginInfo);

        // when & then
        mockMvc.perform(get("/api/v1/auth/kakao-login")
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.user.email").value("kakao@test.com"))
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(header().string("Set-Cookie", containsString("refreshToken=")))
                .andExpect(header().string("Set-Cookie", containsString("HttpOnly")))
                .andDo(print());
    }
}