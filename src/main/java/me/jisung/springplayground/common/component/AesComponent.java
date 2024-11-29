package me.jisung.springplayground.common.component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Slf4j(topic = "AesUtil")
public class AesComponent {

    @Value("${aes_encrypt_key}")
    private String aesKeyString;
    private SecretKey secretKey;

    private static final int BLOCK_SIZE = 16;
    private static final IvParameterSpec IV = new IvParameterSpec(new byte[BLOCK_SIZE]);

    private static final String ALGORITHM_AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_AES = "AES";

    private AesComponent() {}

    @PostConstruct
    public void init() {
        secretKey = generateKey();
    }

    /**
     * Encrypts the value using the given key.
     * @param   plainText 암호화 대상 문자열 값
     * @throws  NoSuchPaddingException extends GeneralSecurityException
     * @throws  NoSuchAlgorithmException extends GeneralSecurityException
     * @throws  InvalidKeyException extends GeneralSecurityException
     * @throws  IllegalBlockSizeException extends GeneralSecurityException
     * @throws  BadPaddingException extends GeneralSecurityException
     * @throws  InvalidAlgorithmParameterException extends GeneralSecurityException
     * */
    public String encrypt(String plainText) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC_PKCS5);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV);

        log.info("IV: {}", IV);

        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Encrypts the value using the given key.
     * @param   key 암호화에 사용될 SecretKey instance
     * @param   plainText 암호화 대상 문자열 값
     * @throws  NoSuchPaddingException extends GeneralSecurityException
     * @throws  NoSuchAlgorithmException extends GeneralSecurityException
     * @throws  InvalidKeyException extends GeneralSecurityException
     * @throws  IllegalBlockSizeException extends GeneralSecurityException
     * @throws  BadPaddingException extends GeneralSecurityException
     * @throws  InvalidAlgorithmParameterException extends GeneralSecurityException
     * */
    public String decrypt(SecretKey key, String plainText) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC_PKCS5);
        cipher.init(Cipher.DECRYPT_MODE, key, IV);
        return new String(cipher.doFinal(Base64.getDecoder().decode(plainText)));
    }

    public SecretKey generateKey() {
        byte[] bytes = new byte[BLOCK_SIZE];
        byte[] keyBytes = aesKeyString.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(keyBytes, 0, bytes, 0, Math.min(BLOCK_SIZE, keyBytes.length));
        return new SecretKeySpec(bytes, ALGORITHM_AES);
    }
}
