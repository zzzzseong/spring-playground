package me.jisung.springplayground.common.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.exception.Api5xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.common.util.JsonUtil;
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
public class HttpRequestSender {

    private final WebClient webClient;

    public final Consumer<HttpHeaders> emptyHeaders = httpHeaders -> {};

    public String post(URI uri, Consumer<HttpHeaders> headers, Object body) {
        String bodyValue = this.getBodyValue(body);
        log.info("[POST REQ] uri: {}, headers: {}, body: {}", uri, this.getHttpHeaders(headers), bodyValue);

        String response = webClient
                .post()
                .uri(uri)
                .headers(headers)
                .bodyValue(bodyValue)
                .exchangeToMono(res -> res.bodyToMono(String.class))
                .onErrorResume(err -> Mono.error(new ApiException(err, Api5xxErrorCode.SERVICE_UNAVAILABLE)))
                .block();

        if(Objects.isNull(response)) throw new ApiException(Api5xxErrorCode.SERVICE_UNAVAILABLE);
        log.info("[POST RES] response: {}", response);
        return response;
    }

    public String get(URI uri, Consumer<HttpHeaders> headers) {
        log.info("[GET REQ] uri: {}, headers: {}", uri, this.getHttpHeaders(headers));
        String response = webClient
                .get()
                .uri(uri)
                .headers(headers)
                .exchangeToMono(res -> res.bodyToMono(String.class))
                .onErrorResume(err -> Mono.error(new ApiException(err, Api5xxErrorCode.SERVICE_UNAVAILABLE)))
                .block();

        if(Objects.isNull(response)) throw new ApiException(Api5xxErrorCode.SERVICE_UNAVAILABLE);
        log.info("[GET RES] response: {}", response);
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

    private HttpHeaders getHttpHeaders(Consumer<HttpHeaders> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.accept(httpHeaders);
        return httpHeaders;
    }

    /**
     * body Object 를 String 으로 변환하는 메서드
     * @param body 변환 대상 Object
     * @return json string converted from body object
     * @throws ApiException INVALID_JSON_FORMAT_5XX: if the body is not valid json format
     * */
    private String getBodyValue(Object body) {
        String bodyValue = JsonUtil.toJson(body);
        JsonUtil.validateJsonFormat(bodyValue);
        return bodyValue;
    }
}