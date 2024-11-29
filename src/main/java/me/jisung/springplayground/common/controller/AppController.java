package me.jisung.springplayground.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.component.AesComponent;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.common.json.SearchRequestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.GeneralSecurityException;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
@Slf4j(topic = "AppController")
public class AppController {

    private final AesComponent aesComponent;

    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return success();
    }

    @GetMapping("/test/aes")
    public ApiResponse<String> testAes(@RequestParam String plainText) {
        try {
            String cipherText = aesComponent.encrypt(plainText);
            log.info("cipherText: {}", cipherText);
            return success(cipherText);
        } catch (GeneralSecurityException e) {
            log.error("encrypt error", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/test/search")
    public ApiResponse<Void> testSearch(SearchRequestDto searchRequestDto) {
        log.info("page: {}", searchRequestDto.getPage());
        log.info("size: {}", searchRequestDto.getSize());
        log.info("query: {}", searchRequestDto.getQuery());

        return success();
    }
}
