package com.github.seunghyeon_tak.price_comparison.common.aop;

import com.github.seunghyeon_tak.price_comparison.common.annotation.LogException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {
    @Around("@annotation(logException)")
    public Object logExceptionHandling(ProceedingJoinPoint joinPoint, LogException logException) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().toShortString();

        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            log.error("[{}.{}] {} - 예외 발생 : {}", className, methodName, logException.value(), ex.getMessage(), ex);
            throw ex;
        }
    }
}
