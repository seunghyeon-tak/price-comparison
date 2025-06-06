package com.github.seunghyeon_tak.price_comparison.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("상품 가격 비교 API")
                        .description("상품 조회 및 가격 알림 관련 API 문서")
                        .version("v1.0.0")
                );
    }
}
