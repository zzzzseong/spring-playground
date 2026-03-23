package me.jisung.springplayground;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Bucket4J 가이드용 테스트.
 * <ul>
 *   <li>Bandwidth: 용량(capacity) + 리필(refill) 정책</li>
 *   <li>tryConsume: 토큰 1개 소비 시도 (비차단)</li>
 *   <li>tryConsumeAndReturnRemaining: 소비 결과 + 남은 토큰/리필 대기 시간</li>
 * </ul>
 */
class Bucket4jGuideTest {

    @Test
    @DisplayName("버킷 생성: capacity=3, 1초마다 3토큰 리필(greedy)")
    void createBucket() {
        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(3) // 버킷 사이즈를 3으로 설정
                .refillGreedy(3, Duration.ofSeconds(1)) // 초당 3개 토큰 리필
                .build();

        Bucket bucket = Bucket.builder()
                .addLimit(bandwidth)
                .build();

        assertThat(bucket.getAvailableTokens()).isEqualTo(3);
    }

    @Test
    @DisplayName("tryConsume: 토큰이 있으면 true, 없으면 false")
    void tryConsume() {
        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(2)
                .refillGreedy(2, Duration.ofSeconds(1))
                .build();

        Bucket bucket = Bucket.builder()
                .addLimit(bandwidth)
                .build();

        assertThat(bucket.tryConsume(1)).isTrue();
        assertThat(bucket.getAvailableTokens()).isEqualTo(1);

        assertThat(bucket.tryConsume(1)).isTrue();
        assertThat(bucket.getAvailableTokens()).isZero();

        assertThat(bucket.tryConsume(1)).isFalse();
    }

    @Test
    @DisplayName("tryConsumeAndReturnRemaining: 소비 성공 시 남은 토큰, 실패 시 리필 대기 시간 확인")
    void tryConsumeAndReturnRemaining() {
        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(2)
                .refillGreedy(2, Duration.ofSeconds(1))
                .build();
        Bucket bucket = Bucket.builder()
                .addLimit(bandwidth)
                .build();

        ConsumptionProbe probe1 = bucket.tryConsumeAndReturnRemaining(1);
        assertThat(probe1.isConsumed()).isTrue();
        assertThat(probe1.getRemainingTokens()).isEqualTo(1);

        ConsumptionProbe probe2 = bucket.tryConsumeAndReturnRemaining(1);
        assertThat(probe2.isConsumed()).isTrue();
        assertThat(probe2.getRemainingTokens()).isZero();

        ConsumptionProbe probe3 = bucket.tryConsumeAndReturnRemaining(1);
        assertThat(probe3.isConsumed()).isFalse();
        assertThat(probe3.getNanosToWaitForRefill()).isGreaterThan(0);
    }

    @Test
    @DisplayName("한 번에 여러 토큰 소비")
    void consumeMultipleTokens() {
        Bandwidth bandwidth = Bandwidth.builder()
                .capacity(5)
                .refillGreedy(5, Duration.ofSeconds(1))
                .build();
        Bucket bucket = Bucket.builder()
                .addLimit(bandwidth)
                .build();

        assertThat(bucket.tryConsume(3)).isTrue();
        assertThat(bucket.getAvailableTokens()).isEqualTo(2);

        assertThat(bucket.tryConsume(3)).isFalse();
        assertThat(bucket.tryConsume(2)).isTrue();
        assertThat(bucket.getAvailableTokens()).isZero();
    }
}
