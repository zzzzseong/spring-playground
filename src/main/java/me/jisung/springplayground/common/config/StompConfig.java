package me.jisung.springplayground.common.config;

import lombok.RequiredArgsConstructor;
import me.jisung.springplayground.common.interceptor.StompChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 웹소켓 프로토콜 및 STOMP 브로커 사용을 위한 설정
 * <br>참고: <a href="https://wans1027.tistory.com/29">STOMP1</a>
 * */
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    private final StompChannelInterceptor stompChannelInterceptor;

    //wss://{host}:{port}/ws/chat 경로로 handshake 및 connection 연결
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
            .addEndpoint("/ws/chat")
            .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 발행 전 내부 컨트롤러로 전달
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
         /* 웹소켓 연결 시 JWT 인증 처리 */
         registration.interceptors(stompChannelInterceptor);
    }
}
