package me.jisung.springplayground.common.interceptor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.component.JwtProvider;
import me.jisung.springplayground.common.constant.SecurityConst;
import me.jisung.springplayground.common.exception.Api4xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.user.entity.UserEntity;
import me.jisung.springplayground.user.repository.UserRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "StompChannelInterceptor")
public class StompChannelInterceptor implements ChannelInterceptor {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        /* 웹소켓 연결 요청 시 JWT 검증 */
        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader(SecurityConst.AUTHORIZATION_HEADER);
            if(!Objects.isNull(authorization) && !authorization.isEmpty()) {
                try {
                    /* 인증 정보 조회 및 사용자 조회 */
                    String accessToken = jwtProvider.getAccessToken(authorization.get(0));
                    Optional<UserEntity> user = userRepository.findByEmail(jwtProvider.getEmail(accessToken));

                    /* 사용자 정보 조회 실패 시 예외처리 */
                    if(user.isEmpty()) return buildErrorResponse(Api4xxErrorCode.NOT_FOUND_AUTHENTICATION.getMessage());
                } catch (ApiException e) {
                    return buildErrorResponse(e.getMessage());
                }
            } else {
                /* 요청 헤더를 찾을 수 없을 시 예외처리 */
                return buildErrorResponse(Api4xxErrorCode.NOT_FOUND_AUTHENTICATION.getMessage());
            }
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    private Message<?> buildErrorResponse(String errorMessage) {
        log.error("{}", errorMessage);

        StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        errorAccessor.setMessage(errorMessage);

        return MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders());
    }
}