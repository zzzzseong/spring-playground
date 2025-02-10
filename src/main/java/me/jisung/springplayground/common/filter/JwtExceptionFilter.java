package me.jisung.springplayground.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.common.exception.ApiErrorVo;
import me.jisung.springplayground.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * JwtAuthorizationFilter 에서 발생한 ApiException 은 ExceptionHandler 에서 처리하지 않는다.
 * JwtExceptionFilter 를 별도로 정의해 클라이언트에 ApiException 반환.
 * */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "JwtExceptionFilter")
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ApiException e) {
            log.error("[RESPONSE FAILED] ApiException - code: {}, message: {}", e.getCode(), e.getMessage());
            if(!Objects.isNull(e.getCause())) log.error("caused by: ", e.getCause());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), ApiResponse.fail(e.getCode(), new ApiErrorVo(e.getMessage())));
        }
    }
}
