package me.jisung.springplayground.common.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Component
@RequiredArgsConstructor
public class S3Helper {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client s3;

    public byte[] downloadFile() {
        return new byte[]{};
    }
}