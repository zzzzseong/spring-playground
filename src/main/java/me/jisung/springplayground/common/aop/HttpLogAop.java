package me.jisung.springplayground.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.util.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Aspect
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j(topic = "ControllerLogAop")
public class HttpLogAop {

    @Pointcut("execution(* me.jisung.springplayground..controller..*(..))")
    public void controller() {}

    /**
     * http request 및 response 로그로 기록하는 컨트롤러 AOP.
     * @see me.jisung.springplayground.common.exception.ApiExceptionHandler
     * - [RESPONSE FAIL]에 대한 로깅은 해당 클래스에서 처리
     */
    @Transactional
    @Around("controller()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = this.getHttpServletRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String body = getBodyString(joinPoint);

        log.info("[REQUEST SUCCESS] class: {}, method: {}, uri: {}, body: {}", getClassName(joinPoint), method, uri, body);

        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            if (!Objects.isNull(result)) log.info("[RESPONSE SUCCESS]: {}", result);
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    private String getClassName(ProceedingJoinPoint joinPoint) {
        String classPath = joinPoint.getSignature().getDeclaringTypeName();
        return classPath.substring(classPath.lastIndexOf(".") + 1);
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
