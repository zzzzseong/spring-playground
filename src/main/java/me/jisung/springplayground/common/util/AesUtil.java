package me.jisung.springplayground.common.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AesUtil")
public class AesUtil {

    private static final int BLOCK_SIZE = 16;
    private static final IvParameterSpec IV = new IvParameterSpec(new byte[BLOCK_SIZE]);

    private static final String ALGORITHM_AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_AES = "AES";

    private AesUtil() { throw new IllegalStateException("utility class can not be instantiated"); }

    /**
     * Encrypts the value using the given key.
     * @param key 암호화에 사용될 SecretKey instance
     * @param value 암호화 대상 문자열 값
     * @throws NoSuchPaddingException extends GeneralSecurityException
     * @throws NoSuchAlgorithmException extends GeneralSecurityException
     * @throws InvalidKeyException extends GeneralSecurityException
     * @throws IllegalBlockSizeException extends GeneralSecurityException
     * @throws BadPaddingException extends GeneralSecurityException
     * @throws InvalidAlgorithmParameterException extends GeneralSecurityException
     * */
    public static String encrypt(SecretKey key, String value) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC_PKCS5);
        cipher.init(Cipher.ENCRYPT_MODE, key, IV);
        byte[] cipherText = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Encrypts the value using the given key.
     * @param key 암호화에 사용될 SecretKey instance
     * @param value 암호화 대상 문자열 값
     * @throws NoSuchPaddingException extends GeneralSecurityException
     * @throws NoSuchAlgorithmException extends GeneralSecurityException
     * @throws InvalidKeyException extends GeneralSecurityException
     * @throws IllegalBlockSizeException extends GeneralSecurityException
     * @throws BadPaddingException extends GeneralSecurityException
     * @throws InvalidAlgorithmParameterException extends GeneralSecurityException
     * */
    public static String decrypt(SecretKey key, String value) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC_PKCS5);
        cipher.init(Cipher.DECRYPT_MODE, key, IV);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(value));
        return new String(plainText);
    }

    public static SecretKey generateKey(String key) {
        byte[] bytes = new byte[BLOCK_SIZE];
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(keyBytes, 0, bytes, 0, Math.min(BLOCK_SIZE, keyBytes.length));
        return new SecretKeySpec(bytes, ALGORITHM_AES);
    }
}
