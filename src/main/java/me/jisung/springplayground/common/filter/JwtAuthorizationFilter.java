package me.jisung.springplayground.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.component.JwtProvider;
import me.jisung.springplayground.common.constant.SecurityConst;
import me.jisung.springplayground.common.exception.Api4xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.common.util.StringUtil;
import me.jisung.springplayground.user.service.UserDetailsServiceImpl;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) {
        try {
            String bearerToken = request.getHeader(SecurityConst.AUTHORIZATION_HEADER);

            if(!StringUtil.isEmpty(bearerToken)) {
                String accessToken = jwtProvider.getAccessToken(bearerToken);
                setAuthentication(jwtProvider.getEmail(accessToken));
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new ApiException(e, Api4xxErrorCode.INVALID_JSON_WEB_TOKEN);
        }
    }

    private void setAuthentication(String email) {
        Authentication authentication = createAuthentication(email);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
