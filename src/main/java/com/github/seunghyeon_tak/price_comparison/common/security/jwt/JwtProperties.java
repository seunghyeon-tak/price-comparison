package com.github.seunghyeon_tak.price_comparison.common.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long accessTokenValidityInMs;
    private long refreshTokenValidityInMs;
}
