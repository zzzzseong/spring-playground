package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.exception.Api5xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SHAUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    private static final String ALGORITHM = "SHA-256";

    public static String generateSHA256Hash(String plainText, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(salt.getBytes());

            byte[] encodedHash = digest.digest(plainText.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(e, Api5xxErrorCode.SHA_ENCRYPTION_FAILED);
        }
    }

    public static String generateSalt() {
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static boolean verityText(String storedHash, String plainText, String salt) {
        return generateSHA256Hash(plainText, salt).equals(storedHash);
    }

    public static void main(String[] args) {
        String salt = generateSalt();
        log.info("Generated salt: {}", salt);

        String plainText = "jisung";
        String hash = generateSHA256Hash(plainText, salt);
        log.info("plainText = {}, hash = {}", plainText, hash);

        if(verityText(hash, plainText, salt)) log.info("Hash verification successful.");
        else log.error("Hash verification failed.");
    }
}