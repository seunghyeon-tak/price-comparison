package com.github.seunghyeon_tak.price_comparison.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kakao")
@Getter
@Setter
public class KakaoOAuthProperties {
    private String clientId;
    private String redirectUri;
    private String tokenUri;
    private String userInfoUri;
}
