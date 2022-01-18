package com.example.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("within(com.example.demo..*)")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("start : {}", joinPoint.toString());

        Object result = joinPoint.proceed();

        log.info("end : {}", joinPoint.toString());

        return result;
    }
}
