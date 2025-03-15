package com.github.seunghyeon_tak.price_comparison.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class LoggingRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        String requestId = request.getRequestId();
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put("REQ_ID", requestId);

        // todo : 추후 JWT에서 userId 추출
        String userId = getUserIdFromRequest(request);
        if (userId == null) {
            userId = "anonymous";
        }
        MDC.put("USER_ID", userId);

        filterChain.doFilter(requestWrapper, response);

        requestWrapper.getParameterMap();

        byte[] content = requestWrapper.getContentAsByteArray();
        if (content.length > 0) {
            String requestBody = new String(content, request.getCharacterEncoding() != null ? request.getCharacterEncoding() : StandardCharsets.UTF_8.name());
            log.info("REQ_ID : {} | USER_ID : {} | 요청 바디 : {}", requestId, userId, requestBody);
        }
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        return null;
    }

}
