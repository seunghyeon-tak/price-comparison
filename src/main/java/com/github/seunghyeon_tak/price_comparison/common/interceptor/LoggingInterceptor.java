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
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        log.info("요청 시작 : REQ_ID : {} | USER_ID : {} | [{}] {} | IP: {}",
                MDC.get("REQ_ID"), MDC.get("USER_ID"), request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("응답 완료 : REQ_ID : {} | USER_ID : {} | [{}] {} | 상태 코드: {} | 실행 시간: {}ms",
                MDC.get("REQ_ID"), MDC.get("USER_ID"), request.getMethod(), request.getRequestURI(), response.getStatus(), duration);

        if (ex != null) {
            log.error("예외 발생 : REQ_ID : {} | USER_ID : {} | [{}] {} | ERROR : {}",
                    MDC.get("REQ_ID"), MDC.get("USER_ID"), request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        }
    }
}
