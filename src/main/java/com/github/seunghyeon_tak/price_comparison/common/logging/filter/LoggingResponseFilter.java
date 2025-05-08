package com.github.seunghyeon_tak.price_comparison.common.logging.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

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
            String encoding = responseWrapper.getCharacterEncoding();
            if (encoding == null || encoding.equalsIgnoreCase("ISO-8859-1")) {
                encoding = StandardCharsets.UTF_8.name();
            }
            String responseBody = new String(content, encoding);
            String prettyResponseBody = formatJson(responseBody);
            log.info("REQ_ID : {} | USER_ID : {} | 응답 바디 : {}", requestId, userId, prettyResponseBody);

            responseWrapper.copyBodyToResponse();
        } finally {
            MDC.clear();
        }
    }

    private String formatJson(String json) {
        try {
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectMapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            return json;
        }
    }
}
