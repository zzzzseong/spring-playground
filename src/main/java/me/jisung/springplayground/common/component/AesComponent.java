package me.jisung.springplayground.common.component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Component
@Slf4j(topic = "AesComponent")
public class AesComponent {

    @Value("${aes_encrypt_key}")
    private String aesKeyString;
    private SecretKey secretKey;

    private static final int BLOCK_SIZE = 16;
    private static final int IV_SIZE = 16;

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
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, this.generateIv());

        byte[] iv = cipher.getIV();
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        ByteBuffer buffer = ByteBuffer.allocate(iv.length + cipherText.length);
        buffer.put(iv);
        buffer.put(cipherText);

        return Base64.getEncoder().encodeToString(buffer.array());
    }

    /**
     * Decrypts the value using the given key.
     * @param   text 복호화 대상 문자열 값
     * @throws  NoSuchPaddingException extends GeneralSecurityException
     * @throws  NoSuchAlgorithmException extends GeneralSecurityException
     * @throws  InvalidKeyException extends GeneralSecurityException
     * @throws  IllegalBlockSizeException extends GeneralSecurityException
     * @throws  BadPaddingException extends GeneralSecurityException
     * @throws  InvalidAlgorithmParameterException extends GeneralSecurityException
     * */
    public String decrypt(String text) throws GeneralSecurityException {
        byte[] bytes = Base64.getDecoder().decode(text);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        byte[] iv = new byte[IV_SIZE];
        byteBuffer.get(iv);
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC_PKCS5);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private SecretKey generateKey() {
        byte[] bytes = new byte[BLOCK_SIZE];
        byte[] keyBytes = aesKeyString.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(keyBytes, 0, bytes, 0, Math.min(BLOCK_SIZE, keyBytes.length));
        return new SecretKeySpec(bytes, ALGORITHM_AES);
    }
}
