package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * AES(Advanced Encryption Standard) Utility Class
 * <br> block-size supports AES-128(16byte key), AES-192(24byte key), AES-256(32byte key)
 * */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AesUtil {

    private static final int BLOCK_SIZE_BYTE = 32;
    private static final int IV_SIZE_BYTE = 16;

    private static final String ALGORITHM_AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_AES = "AES";


    /**
     * Encrypts the value using the given key.
     * @param   plainText text to be encrypted
     * @throws  NoSuchPaddingException extends GeneralSecurityException
     * @throws  NoSuchAlgorithmException extends GeneralSecurityException
     * @throws  InvalidKeyException extends GeneralSecurityException
     * @throws  IllegalBlockSizeException extends GeneralSecurityException
     * @throws  BadPaddingException extends GeneralSecurityException
     * @throws  InvalidAlgorithmParameterException extends GeneralSecurityException
     * @return  encrypted text
     * */
    public static String encrypt(SecretKey secretKey, String plainText) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC_PKCS5);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, generateIv());

        byte[] iv = cipher.getIV();
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        ByteBuffer buffer = ByteBuffer.allocate(iv.length + cipherText.length);
        buffer.put(iv);
        buffer.put(cipherText);

        return Base64.getEncoder().encodeToString(buffer.array());
    }

    /**
     * Decrypts the value using the given key.
     * @param   encryptedText text to be decrypted
     * @throws  NoSuchPaddingException extends GeneralSecurityException
     * @throws  NoSuchAlgorithmException extends GeneralSecurityException
     * @throws  InvalidKeyException extends GeneralSecurityException
     * @throws  IllegalBlockSizeException extends GeneralSecurityException
     * @throws  BadPaddingException extends GeneralSecurityException
     * @throws  InvalidAlgorithmParameterException extends GeneralSecurityException
     * @return  decrypted text
     * */
    public static String decrypt(SecretKey secretKey, String encryptedText) throws GeneralSecurityException {
        byte[] bytes = Base64.getDecoder().decode(encryptedText);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        byte[] iv = new byte[IV_SIZE_BYTE];
        byteBuffer.get(iv);
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC_PKCS5);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }

    /**
     * Generate a secret key for AES encryption/decryption.
     * @param rawKey The key to be used for AES encryption. (required 16, 24, 32 bytes)
     * @return SecretKey
     * */
    public static SecretKey generateSecretKey(String rawKey) {
        byte[] bytes = new byte[BLOCK_SIZE_BYTE];
        byte[] keyBytes = rawKey.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(keyBytes, 0, bytes, 0, Math.min(BLOCK_SIZE_BYTE, keyBytes.length));
        return new SecretKeySpec(bytes, ALGORITHM_AES);
    }

    /**
     * Generate a random IV for AES encryption/decryption. IV size must be 16 bytes.
     * @return IvParameterSpec
     * */
    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_SIZE_BYTE];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
