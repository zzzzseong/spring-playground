package me.jisung.springplayground.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.common.exception.Api5xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.common.util.AesUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
@Slf4j(topic = "AppController")
public class AppController {

    @Value("${aes_encrypt_key}")
    private String secretKeyStr;

    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return success();
    }

    @GetMapping("/test/aes/enc")
    public ApiResponse<String> testAesEnc(@RequestParam("plainText") String plainText) {

        SecretKey secretKey = AesUtil.generateSecretKey(secretKeyStr);
        try {
            String cipherText = AesUtil.encrypt(secretKey, plainText);
            log.info("cipherText: {}", cipherText);
            return success(cipherText);
        } catch (GeneralSecurityException e) {
            log.error("error occurred while encrypting", e);
            throw new ApiException(Api5xxErrorCode.AES_ENCRYPTION_FAILED);
        }
    }

    @GetMapping("/test/aes/dec")
    public ApiResponse<String> testAesDec(@RequestParam("cipherText") String cipherText) {
        SecretKey secretKey = AesUtil.generateSecretKey(secretKeyStr);
        try {
            String plainText = AesUtil.decrypt(secretKey, cipherText);
            log.info("plainText: {}", plainText);
            return success(plainText);
        } catch (GeneralSecurityException e) {
            log.error("error occurred while decrypting", e);
            throw new ApiException(Api5xxErrorCode.AES_DECRYPTION_FAILED);
        }
    }

    @GetMapping("/test/pageable")
    public ApiResponse<Void> testPageable(@PageableDefault(page = 1, size = 100) Pageable pageable) {
        log.info("pageable: {}", pageable);
        return success();
    }
}
