package me.jisung.springplayground.common.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    /* 서버 연결 시도 시 타임아웃 설정 - 5s */
    private static final int CONNECTION_TIMEOUT_MILLIS = 5000;

    /* 서버로부터 응답 데이터를 읽는 데 걸리는 시간에 대한 타임아웃 설정 - 10s */
    private static final int READ_TIMEOUT_SECONDS = 10;

    /* 서버로 요청 데이터를 쓰는 데 걸리는 시간에 대한 타임아웃 설정 - 10s */
    private static final int WRITE_TIMEOUT_SECONDS = 10;

    /* HTTP 요청을 보낸 시점부터 응답을 받기까지에 대한 전체 시간 타임아웃 설정 - 5s */
    private static final int RESPONSE_TIMEOUT_SECONDS = 5;

    /* 요청, 응답에서 허용하는 최대 버퍼 크기 설정 - 2MB */
    private static final int MAX_IN_MEMORY_SIZE = 2 * 1024 * 1024;

    /* 동일한 IP 주소에 대해 HTTP 커넥션 재사용 여부 설정 */
    private static final boolean KEEP_ALIVE = true;


    @Bean
    public WebClient webClient(){
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(MAX_IN_MEMORY_SIZE))
                .build();

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    private HttpClient httpClient(){
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT_MILLIS)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_SECONDS))
                )
                .responseTimeout(Duration.ofSeconds(RESPONSE_TIMEOUT_SECONDS))
                .keepAlive(KEEP_ALIVE);
    }
}
