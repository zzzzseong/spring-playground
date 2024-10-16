package me.jisung.springplayground.common.util;

import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.exception.Api5xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j(topic = "AesUtil")
public class AesUtil {

    private static final int BLOCK_SIZE = 16;
    private static final IvParameterSpec IV = new IvParameterSpec(new byte[BLOCK_SIZE]);

    private static final String ALGORITHM_AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_AES = "AES";

    private AesUtil() { throw new IllegalStateException("utility class can not be instantiated"); }

    public static String encrypt(SecretKey key, String value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC_PKCS5);
            cipher.init(Cipher.ENCRYPT_MODE, key, IV);
            byte[] cipherText = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);

        } catch (NoSuchPaddingException e) {
            log.error("No such padding exception during encryption", e);
            throw new ApiException(e, Api5xxErrorCode.ENCRYPTION_NO_SUCH_PADDING);
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm exception during encryption", e);
            throw new ApiException(e, Api5xxErrorCode.ENCRYPTION_NO_SUCH_ALGORITHM);
        } catch (InvalidKeyException e) {
            log.error("Invalid key exception during encryption", e);
            throw new ApiException(e, Api5xxErrorCode.ENCRYPTION_INVALID_KEY);
        } catch (IllegalBlockSizeException e) {
            log.error("Illegal block size exception during encryption", e);
            throw new ApiException(e, Api5xxErrorCode.ENCRYPTION_ILLEGAL_BLOCK_SIZE);
        } catch (BadPaddingException e) {
            log.error("Bad padding exception during encryption", e);
            throw new ApiException(e, Api5xxErrorCode.ENCRYPTION_BAD_PADDING);
        } catch (InvalidAlgorithmParameterException e) {
            log.error("Invalid algorithm parameter exception during encryption", e);
            throw new ApiException(e, Api5xxErrorCode.ENCRYPTION_INVALID_ALGORITHM_PARAMETER);
        }
    }

    public static String decrypt(SecretKey key, String value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC_PKCS5);
            cipher.init(Cipher.DECRYPT_MODE, key, IV);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(value));
            return new String(plainText);
        } catch (NoSuchPaddingException e) {
            log.error("No such padding exception during decryption", e);
            throw new ApiException(e, Api5xxErrorCode.DECRYPTION_NO_SUCH_PADDING);
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm exception during decryption", e);
            throw new ApiException(e, Api5xxErrorCode.DECRYPTION_NO_SUCH_ALGORITHM);
        } catch (InvalidKeyException e) {
            log.error("Invalid key exception during decryption", e);
            throw new ApiException(e, Api5xxErrorCode.DECRYPTION_INVALID_KEY);
        } catch (IllegalBlockSizeException e) {
            log.error("Illegal block size exception during decryption", e);
            throw new ApiException(e, Api5xxErrorCode.DECRYPTION_ILLEGAL_BLOCK_SIZE);
        } catch (BadPaddingException e) {
            log.error("Bad padding exception during decryption", e);
            throw new ApiException(e, Api5xxErrorCode.DECRYPTION_BAD_PADDING);
        } catch (InvalidAlgorithmParameterException e) {
            log.error("Invalid algorithm parameter exception during decryption", e);
            throw new ApiException(e, Api5xxErrorCode.DECRYPTION_INVALID_ALGORITHM_PARAMETER);
        }
    }

    public static SecretKey generateKey(String key) {
        byte[] bytes = new byte[BLOCK_SIZE];
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(keyBytes, 0, bytes, 0, Math.min(BLOCK_SIZE, keyBytes.length));
        return new SecretKeySpec(bytes, ALGORITHM_AES);
    }

}
