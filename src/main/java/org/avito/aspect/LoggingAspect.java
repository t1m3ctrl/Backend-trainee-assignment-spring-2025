package org.avito.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(public * org.avito.service..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("Entering: {} with args {}", methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed(); // вызываем сам метод
            long duration = System.currentTimeMillis() - start;

            log.info("Exiting: {} with result = {}, took {} ms", methodName, result, duration);
            return result;
        } catch (Throwable ex) {
            log.error("‼ Exception in {}: {}", methodName, ex.getMessage(), ex);
            throw ex;
        }
    }
}