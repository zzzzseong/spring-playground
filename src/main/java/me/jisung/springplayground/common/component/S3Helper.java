package me.jisung.springplayground.common.component;

import me.jisung.springplayground.common.util.AesUtil;
import me.jisung.springplayground.common.util.FileUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

@Component
public class S3Helper {
    
    
    
    public static void main(String[] args) {

        MultipartFile file = FileUtil.getMultipartFileFromClassPath("logback.xml");

        String filename = file.getOriginalFilename();

        int commaIdx = filename.lastIndexOf('.');
        if(commaIdx == -1) {
            throw new IllegalArgumentException("파일 확장자가 없습니다.");
        }

        String name = filename.substring(0, commaIdx);
        String ext = filename.substring(commaIdx + 1);

        System.out.println("name = " + name);
        System.out.println("ext = " + ext);
        SecretKey secretKey = AesUtil.generateSecretKey("springplayground");

        try {
            String encrypt = AesUtil.encrypt(secretKey, name);
            System.out.println("newFilename = " + encrypt + "." + ext);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }




        // convert name 2 unique

    }
}
