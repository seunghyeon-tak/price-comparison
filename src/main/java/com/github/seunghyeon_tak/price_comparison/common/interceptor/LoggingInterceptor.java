package com.github.seunghyeon_tak.price_comparison.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.nanoTime());

        log.info("요청 시작 : REQ_ID : {} | USER_ID : {} | [{}] {} | IP: {}",
                MDC.get("REQ_ID"), MDC.get("USER_ID"), request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.nanoTime();
        long executionTimeMs = (endTime - startTime) / 1_000_000;

        log.info("응답 완료 : REQ_ID : {} | USER_ID : {} | [{}] {} | 상태 코드: {} | 실행 시간: {}ms",
                MDC.get("REQ_ID"), MDC.get("USER_ID"), request.getMethod(), request.getRequestURI(), response.getStatus(), executionTimeMs);

        if (ex != null) {
            log.error("예외 발생 : REQ_ID : {} | USER_ID : {} | [{}] {} | ERROR : {}",
                    MDC.get("REQ_ID"), MDC.get("USER_ID"), request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        }
    }
}
