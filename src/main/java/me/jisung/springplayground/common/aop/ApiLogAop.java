package me.jisung.springplayground.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.util.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * validation -> aop -> controller -> exception throws -> aop -> exception handler
 * */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j(topic = "ControllerLogAop")
public class ApiLogAop {

    @Pointcut("execution(* me.jisung.springplayground..controller..*(..))")
    public void controller() {}

    @Pointcut("!execution(* me.jisung.springplayground.common.controller.StompMessageController..*(..))")
    public void websocket() {}

    /**
     * http request 및 response 로그로 기록하는 컨트롤러 AOP.
     * @see me.jisung.springplayground.common.exception.ApiExceptionHandler
     * - [RESPONSE FAIL]에 대한 로깅은 해당 클래스에서 처리
     */
    @Around("controller() && websocket()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = this.getHttpServletRequest();
        String method   = request.getMethod();
        String uri      = request.getRequestURI();
        String body     = getBodyString(joinPoint);

        log.info("[API REQUEST SUCCESS] method: {}, uri: {}, body: {}", method, uri, body);

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

    private String getBodyString(ProceedingJoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse) && !(arg instanceof BindingResult)) {
                sb.append(JsonUtil.toJson(arg));
            }
        }
        return sb.toString();
    }
}
