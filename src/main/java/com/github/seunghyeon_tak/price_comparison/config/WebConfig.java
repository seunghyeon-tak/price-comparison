package com.github.seunghyeon_tak.price_comparison.config;

import com.github.seunghyeon_tak.price_comparison.common.logging.filter.LoggingRequestFilter;
import com.github.seunghyeon_tak.price_comparison.common.logging.filter.LoggingResponseFilter;
import com.github.seunghyeon_tak.price_comparison.common.logging.interceptor.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final LoggingInterceptor loggingInterceptor;

    @Bean
    public FilterRegistrationBean<LoggingRequestFilter> loggingRequestFilter() {
        FilterRegistrationBean<LoggingRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(1);
        registrationBean.setFilter(new LoggingRequestFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LoggingResponseFilter> loggingResponseFilter() {
        FilterRegistrationBean<LoggingResponseFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(Integer.MAX_VALUE - 1);
        registrationBean.setFilter(new LoggingResponseFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .excludePathPatterns("/health", "/error", "/favicon.ico");
    }
}
