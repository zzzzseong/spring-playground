package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.exception.Api4xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.user.entity.UserDetailsImpl;
import me.jisung.springplayground.user.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {
    private static final String ANONYMOUS_USER = "anonymousUser";

    public static UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        /* 인증 정보가 없거나 인증되지 않은 사용자라면 예외처리 */
        if (Objects.isNull(authentication) || Objects.equals(ANONYMOUS_USER, authentication.getPrincipal())) {
            throw new ApiException(Api4xxErrorCode.NOT_FOUND_AUTHENTICATION, "요청에 대한 사용자 인증 정보를 찾을 수 없습니다.");
        }

        return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
    }
}
