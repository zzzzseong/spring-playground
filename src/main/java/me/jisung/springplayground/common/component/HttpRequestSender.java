package me.jisung.springplayground.common.component;

import java.net.URI;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.util.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "WebClientUtil")
public class HttpRequestSender {

    private final WebClient webClient;

    public static final String DEFAULT_BODY_VALUE = "{}";
    public static final Consumer<HttpHeaders> emptyHeaders = httpHeaders -> {};

    public String post(URI uri, Consumer<HttpHeaders> headers, Object body) throws HttpStatusCodeException {
        String bodyValue = Objects.isNull(body) ? DEFAULT_BODY_VALUE : JsonUtil.toJson(body);
        log.info("[POST REQ] uri: {}, headers: {}, body: {}", uri, this.getHttpHeaders(headers), bodyValue);

        String response = webClient
            .post()
            .uri(uri)
            .headers(headers)
            .bodyValue(bodyValue)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, err -> Mono.error(new HttpClientErrorException(err.statusCode())))
            .onStatus(HttpStatusCode::is5xxServerError, err -> Mono.error(new HttpServerErrorException(err.statusCode())))
            .bodyToMono(String.class)
            .block();

        if(Objects.isNull(response)) throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE);
        log.info("[POST RES] response: {}", response);
        return response;
    }

    public String get(URI uri, Consumer<HttpHeaders> headers) throws HttpStatusCodeException {
        log.info("[GET REQ] uri: {}, headers: {}", uri, this.getHttpHeaders(headers));

        String response = webClient
            .get()
            .uri(uri)
            .headers(headers)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, err -> Mono.error(new HttpClientErrorException(err.statusCode())))
            .onStatus(HttpStatusCode::is5xxServerError, err -> Mono.error(new HttpServerErrorException(err.statusCode())))
            .bodyToMono(String.class)
            .block();

        if(Objects.isNull(response)) throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE);
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
}