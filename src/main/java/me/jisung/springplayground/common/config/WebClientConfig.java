package me.jisung.springplayground.common.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;


@Configuration
@Slf4j(topic = "WebClientConfig")
public class WebClientConfig {

    @Value("${webclient.connection.timeoutMillis:10000}")
    private int connectionTimeoutMillis;

    @Value("${webclient.read.timeoutSeconds:10}")
    private int readTimeoutSeconds;

    @Value("${webclient.write.timeoutSeconds:10}")
    private int writeTimeoutSeconds;

    @Value("${webclient.response.timeoutSeconds:5}")
    private int responseTimeoutSeconds;

    @Value("${webclient.maxInMemorySize:52428800}") // 50MB
    private int maxInMemorySize;


    @Bean
    public WebClient webClient(){
        // MaxInMemorySize
        // ExchangeStrategies: 응답의 최대 메모리 크기를 50MB로 설정
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxInMemorySize))
                .build();

        //Logging
        //개발 진행 시 Request/Response 정보를 상세히 확인하기 위해서
        exchangeStrategies
                .messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true));

        // Request header 출력필터
        ExchangeFilterFunction requestFilter = ExchangeFilterFunction
                .ofRequestProcessor(this::exchangeFilterRequestProcessor);

        // Response header 출력필터
        ExchangeFilterFunction responseFilter = ExchangeFilterFunction
                .ofResponseProcessor(this::exchangeFilterResponseProcessor);


        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient())) // HTTP 클라이언트 라이브러리 세팅
                .exchangeStrategies(exchangeStrategies)
                .filter(requestFilter)
                .filter(responseFilter)
                // 기본 헤더값.
                .defaultHeader(HttpHeaders.CONTENT_TYPE,"application/x-www-form-urlencoded; charset=UTF-8")
                .build()
                ;
    }

    private HttpClient httpClient(){
        return HttpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutMillis) // connection timeout
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeoutSeconds)) //read timeout
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeoutSeconds)) // write timeout
                )
                .responseTimeout(Duration.ofSeconds(responseTimeoutSeconds)) // response timeout for all requests
                .keepAlive(false) // 커넥션 재사용 금지
                ;
    }

    private Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse clientResponse) {
        if (log.isDebugEnabled()) {
            log.debug("Response: {}, {}", clientResponse.statusCode(), clientResponse.bodyToMono(String.class));
            clientResponse.headers()
                    .asHttpHeaders()
                    .forEach((name, values) ->
                            values.forEach(value -> log.debug("{} : {}", name, value)));
        }
        return Mono.just(clientResponse);
    }

    private Mono<ClientRequest> exchangeFilterRequestProcessor(ClientRequest clientRequest) {
        if (log.isDebugEnabled()) {
            log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) ->
                            values.forEach(value -> log.debug("{} : {}", name, value)));
        }
        return Mono.just(clientRequest);
    }
}
