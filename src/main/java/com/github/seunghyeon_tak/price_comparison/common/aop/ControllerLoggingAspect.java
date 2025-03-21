package com.github.seunghyeon_tak.price_comparison.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Around("@annotation(controllerLoggable)")
    public Object logControllerException(ProceedingJoinPoint joinPoint, ControllerLoggable controllerLoggable) throws Throwable {
        long start = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        Object result;
        try {
            log.info("[{}.{}] 실행 시작 - {}", className, methodName, controllerLoggable.value());
            log.info("입력 파라미터: {}", Arrays.toString(args));
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.error("[{}.{}] 실행 오류 - {}", className, methodName, e.getMessage());
            throw e;
        }

        long end = System.currentTimeMillis();
        long executionTimeMs = (end - start) / 1_000_000;

        String resultAsJson = formatJson(result);

        log.info("[{}.{}] 실행 완료 - 시간: {}ms, 반환 값: {}", className, methodName, executionTimeMs, resultAsJson);

        return result;
    }

    private String formatJson(Object result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            return result.toString();
        }
    }
}
