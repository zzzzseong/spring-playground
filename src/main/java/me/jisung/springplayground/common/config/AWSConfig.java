package me.jisung.springplayground.common.config;

import me.jisung.springplayground.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Configuration
public class AWSConfig {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.accessKey:#{null}}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey:#{null}}}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        S3ClientBuilder builder = S3Client.builder().region(Region.of(region));

        // EC2에서는 IAM 역할이 자동으로 자격 증명을 제공하므로 인증정보(Credentials) 명시적으로 설정할 필요 없음
        // AWS SDK v2가 자동으로 EC2 인스턴스 메타데이터 서비스(IMDS)를 통해 찾음
        if(!StringUtil.isEmpty(accessKey)) return builder.build();
        else return builder.credentialsProvider(StaticCredentialsProvider.create(awsCredentials)).build();
    }
}
