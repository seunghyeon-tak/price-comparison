package com.github.seunghyeon_tak.price_comparison.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class LoggingResponseFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String requestId = MDC.get("REQ_ID");
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
            MDC.put("REQ_ID", requestId);
        }

        String userId = MDC.get("USER_ID");
        if (userId == null) {
            userId = "anonymous";
        }

        try {
            filterChain.doFilter(request, responseWrapper);

            byte[] content = responseWrapper.getContentAsByteArray();
            if (content.length > 0) {
                String responseBody = new String(content, responseWrapper.getCharacterEncoding() != null ? responseWrapper.getCharacterEncoding() : StandardCharsets.UTF_8.name());
                log.info("REQ_ID : {} | USER_ID : {} | 응답 바디 : {}", requestId, userId, responseBody);
            }

            responseWrapper.copyBodyToResponse();
        } finally {
            MDC.clear();
        }
    }
}
