package me.jisung.springplayground.common.controller;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.component.AsyncMailSender;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.common.exception.Api5xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.common.util.AesUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.util.Arrays;

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


    //    [Application Data]
    //            ↓
    //    [ TCP Header + Data ] = TCP Segment
    //            ↓
    //    [ IP Header + TCP Segment ] = IP Packet
    //            ↓
    //    [ Ethernet Header + IP Packet ] = Frame (링크 계층)
    @PostMapping("/test/serialize")
    public ApiResponse<Void> serialize() {

        JsonObject jsonObj = new JsonObject();
//        jsonObj.addProperty("username", "playground");
//        jsonObj.addProperty("password", "playground");
        jsonObj.addProperty("username","jisung");

        log.info("@@@@@@ Application Layer @@@@@@");
        log.info("[1] JSON Object: {}", jsonObj);

        byte[] bytes = jsonObj.toString().getBytes();
        log.info("[2] JSON as byte array: {}", Arrays.toString(bytes));

        String[] hexArray = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) hexArray[i] = Integer.toHexString(bytes[i]);
        log.info("[3] Hex representation of byte array: {}", Arrays.toString(hexArray));

        log.info("@@@@@@ Transport Layer @@@@@@");
        byte[] tcpHeader = new byte[] {
                // Source Port (12345) = 0x30 0x39
                0x30, 0x39,
                // Destination Port (8080) = 0x1F 0x90
                0x1F, (byte)0x90,
                // Sequence Number (예시: 1) = 0x00 0x00 0x00 0x01
                0x00, 0x00, 0x00, 0x01,
                // Acknowledgment Number (예시: 0)
                0x00, 0x00, 0x00, 0x00,
                // Data Offset (5) + Reserved + Flags (SYN=1)
                0x50, 0x02,
                // Window Size (예시)
                0x71, 0x10,
                // Checksum (0으로 채움)
                0x00, 0x00,
                // Urgent Pointer
                0x00, 0x00,
        };

        String[] tcpSegment = new String[tcpHeader.length + hexArray.length];
        for(int i = 0; i < tcpHeader.length; i++) tcpSegment[i] = Integer.toHexString(tcpHeader[i]);
        for(int i = 0; i < hexArray.length; i++) tcpSegment[i + tcpHeader.length] = hexArray[i];
        log.info("[4] TCP Segment: {}", Arrays.toString(tcpSegment));


        return success();
    }
}
