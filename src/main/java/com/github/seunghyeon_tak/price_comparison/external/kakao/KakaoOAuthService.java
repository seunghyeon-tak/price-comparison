package com.github.seunghyeon_tak.price_comparison.external.kakao;

import com.github.seunghyeon_tak.price_comparison.config.KakaoOAuthProperties;
import com.github.seunghyeon_tak.price_comparison.external.kakao.dto.KakaoAccessTokenResponse;
import com.github.seunghyeon_tak.price_comparison.external.kakao.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {
    private final KakaoOAuthProperties kakaoOAuthProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoAccessTokenResponse kakaoRequestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthProperties.getClientId());
        params.add("redirect_uri", kakaoOAuthProperties.getRedirectUri());
        params.add("code", code);

        HttpEntity<?> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoAccessTokenResponse> response = restTemplate.exchange(
                kakaoOAuthProperties.getTokenUri(),
                HttpMethod.POST,
                request,
                KakaoAccessTokenResponse.class
        );

        return response.getBody();
    }

    public KakaoUserInfo kakaoRequestUserInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                kakaoOAuthProperties.getUserInfoUri(),
                HttpMethod.GET,
                request,
                KakaoUserInfo.class
        );

        return response.getBody();
    }
}
