package me.jisung.springplayground.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstant {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX        = "Bearer ";

}
