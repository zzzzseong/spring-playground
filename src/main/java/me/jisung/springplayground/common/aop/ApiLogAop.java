package me.jisung.springplayground.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.util.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * validation -> aop -> controller -> exception throws -> aop -> exception handler
 * */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLogAop {


    @Pointcut("execution(* me.jisung.springplayground..controller..*(..))")
    public void controllers() {}

    @Pointcut("!execution(* me.jisung.springplayground.chat.controller.MessageController..*(..))")
    public void socketController() {}


    /**
     * HTTP 요청 및 응답 로그를 기록하는 AOP 입니다.
     */
    @Around("controllers() && socketController()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = this.getHttpServletRequest();
        String method   = request.getMethod();
        String uri      = request.getRequestURI();

        log.info("[API REQUEST SUCCESS] method: {}, uri: {}", method, uri);

        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            if (!Objects.isNull(result)) log.info("[API RESPONSE SUCCESS]: {}", JsonUtil.toJson(result));
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
