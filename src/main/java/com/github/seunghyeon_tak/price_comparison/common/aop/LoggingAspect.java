package com.github.seunghyeon_tak.price_comparison.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.seunghyeon_tak.price_comparison.common.annotation.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Around("@annotation(loggable)")
    public Object logMethodException(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("[{}.{}] 실행 시작 - {}", className, methodName, loggable.value());
        log.info("입력 파라미터: {}", Arrays.toString(args));

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
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
