package me.jisung.springplayground.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OtpUtil {

    private static final int BUFFER_SIZE_BYTE = 32;
    private static final String DOMAIN = "playground.io";
    private static final String OTP_URL_FORMAT = "https://quickchart.io/chart?cht=qr&chs=200x200&chl=otpauth://totp/%s@%s?secret=%s&chld=H|0";
    private static final String OTP_AUTH_URL_FORMAT = "otpauth://totp/%s@%s?secret=%s&issuer=%s";
    private static final int QR_CODE_SIZE = 300;

    private static final SecureRandom r = new SecureRandom();
    private static final Base32 codec = new Base32();

    public static String generateSecretKey() {
        byte[] buffer = new byte[BUFFER_SIZE_BYTE];
        r.nextBytes(buffer);

        byte[] secretKey = Arrays.copyOf(buffer, 5);
        return new String(codec.encode(secretKey));
    }

    public static String generateOtpUrl(String siteid, String otpKey) {
        return String.format(OTP_URL_FORMAT, siteid, DOMAIN, otpKey);
    }

    /**
     * OTP 인증 URL을 생성합니다.
     * @param siteid 사이트 ID
     * @param otpKey OTP 시크릿 키
     * @return otpauth://totp/ 형식의 URL
     */
    public static String generateOtpAuthUrl(String siteid, String otpKey) {
        return String.format(OTP_AUTH_URL_FORMAT, siteid, DOMAIN, otpKey, DOMAIN);
    }

    /**
     * OTP 인증 URL을 QR 코드 이미지로 생성하고 Base64로 인코딩하여 반환합니다.
     * @param siteid 사이트 ID
     * @param otpKey OTP 시크릿 키
     * @return Base64로 인코딩된 QR 코드 이미지 (data:image/png;base64,... 형식)
     */
    public static String generateQrCodeBase64(String siteid, String otpKey) {
        try {
            String otpAuthUrl = generateOtpAuthUrl(siteid, otpKey);
            
            // QR 코드 생성 설정
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            // QR 코드 생성
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthUrl, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);

            // 이미지를 바이트 배열로 변환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] qrCodeBytes = outputStream.toByteArray();

            // Base64로 인코딩
            String base64Image = Base64.getEncoder().encodeToString(qrCodeBytes);
            return "data:image/png;base64," + base64Image;
        } catch (WriterException | IOException e) {
            log.error("QR 코드 생성 실패 - siteid: {}, error: {}", siteid, e.getMessage(), e);
            throw new RuntimeException("QR 코드 생성에 실패했습니다.", e);
        }
    }

    public static boolean verifyOtp(int otp, String otpKey) {
        long l = new Date().getTime();
        long ll =  l / 30000;

        byte[] decodedKey = codec.decode(otpKey);

        try {
            // Window is used to check codes generated in the near past.
            // You can use this value to tune how far you're willing to go.
            int window = 3;
            for (int i = -window; i <= window; ++i) {
                long hash = extractHash(decodedKey, ll + i);

                if (hash == otp) {
                    return true;
                }
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Exception occured during verity OTP", e);
        }

        return false;
    }

    private static int extractHash(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) data[i] = (byte) value;

        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[20 - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }
}
