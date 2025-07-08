package com.github.seunghyeon_tak.price_comparison.config;

import com.github.seunghyeon_tak.price_comparison.common.security.jwt.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
}
