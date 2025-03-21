package com.github.seunghyeon_tak.price_comparison.common.aop;

import com.github.seunghyeon_tak.price_comparison.common.annotation.BusinessLoggable;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class BusinessLoggingAspect {
    @Around("@annotation(businessLoggable)")
    public Object logBusinessException(ProceedingJoinPoint joinPoint, BusinessLoggable businessLoggable) throws Throwable {
        log.info("[비지니스 로직] {} 실행 시작", joinPoint.getSignature().toShortString());

        Object result = joinPoint.proceed();

        log.info("[비지니스 로직] {} 실행 완료", joinPoint.getSignature().toShortString());
        return result;
    }
}
