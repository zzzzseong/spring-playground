package me.jisung.springplayground.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.exception.ApiErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "WebClientUtil")
public class WebClientUtil {

    public final Consumer<HttpHeaders> emptyHeaders = httpHeaders -> {};

    private final WebClient webClient;

    public String get(URI uri, Consumer<HttpHeaders> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.accept(httpHeaders);

        log.info("[REQUEST GET] uri: {}, headers: {}", uri, httpHeaders);
        String response = webClient
                .get()
                .uri(uri)
                .headers(headers)
                .exchangeToMono(res -> res.bodyToMono(String.class))
                .onErrorResume(err -> Mono.error(new ApiException(err, ApiErrorCode.SERVICE_UNAVAILABLE)))
                .block();

        if(Objects.isNull(response)) throw new ApiException(ApiErrorCode.SERVICE_UNAVAILABLE);
        log.info("[RESPONSE GET] response: {}", response);
        return response;
    }

    public URI buildUri(String scheme, String host) {
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .build()
                .toUri();
    }
    public URI buildUri(String scheme, String host, String path) {
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path(path)
                .build()
                .toUri();
    }
    public URI buildUri(String scheme, String host, String path, MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path(path)
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
