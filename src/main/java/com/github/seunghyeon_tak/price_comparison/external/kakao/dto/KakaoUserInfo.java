package com.github.seunghyeon_tak.price_comparison.external.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class KakaoUserInfo {
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class KakaoAccount {
        private String email;
        private String profile;

        @Getter
        public static class Profile {
            private String nickname;
        }
    }
}
