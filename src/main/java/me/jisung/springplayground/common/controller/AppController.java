package me.jisung.springplayground.common.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.component.AsyncMailSender;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.common.exception.Api5xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.common.util.AesUtil;
import me.jisung.springplayground.common.util.JsonUtil;
import me.jisung.springplayground.product.data.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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

    private final AsyncMailSender asyncMailSender;
    private final TemplateEngine templateEngine;

    @GetMapping("/test/mail")
    public ApiResponse<Void> testMail(@RequestParam String to) {

        Context context = new Context();
        context.setVariable("username", "jisung");
        context.setVariable("loginUrl", "https://www.naver.com");

        String content = templateEngine.process("mail/signup", context);

        asyncMailSender.sendMail(to, "메일 발송 테스트", content);

        return success();
    }


    /**
     * 블로그 정리 필요
     * 클라이언트로 부터 요청이 들어올때 DTO 클래스에 기본 생성자를 찾는데, 기본 생성자가 없으면 다른 생성자로 대체한다.
     * */
    @PostMapping("/test/dto-constructor")
    public ApiResponse<Void> testDtoConstructor(@RequestBody TestDto testDto) {

        log.info("testDto: {}", testDto);

        return success();
    }

    @Getter
    public static class TestDto {
        private String val1;
        private String val2;

        // 에러 발생
        public TestDto(ProductDTO dto) {
            dto.getPrice();
        }

        @Override
        public String toString() {
            return JsonUtil.toJson(this);
        }
    }
}
