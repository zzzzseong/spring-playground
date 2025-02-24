package me.jisung.springplayground.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        /* 기본적으로 유지되는 스레드 수 - 스레드 풀 크기를 10으로 설정 */
        executor.setCorePoolSize(10);

        /* 필요에 따라 생성될 수 있는 최대 스레드 수 - 최대 스레드 풀 크기를 20으로 설정 */
        executor.setMaxPoolSize(20);

        /* 대기열에 저장될 수 있는 최대 작업 수 - 작업 대기열의 용량을 100으로 설정  */
        executor.setQueueCapacity(100);

        /* 작업이 대기열에 가득 찼을 때 처리할 정책 - CallerRunsPolicy(작업 대기열이 가득 찼을 경우 호출 스레드가 직접 작업을 실행하도록 설정) */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        /* 애플리케이션 종료 시 남아 있는 작업이 완료될 때까지 대기하도록 설정 */
        executor.setWaitForTasksToCompleteOnShutdown(true);

        /* 애플리케이션 종료 시 최대 30초 동안 대기 */
        executor.setAwaitTerminationSeconds(30);

        /* 생성되는 스레드의 이름 접두사를 설정 - 로그 확인을 위함 */
        executor.setThreadNamePrefix("AsyncTaskExecutor-");
        executor.initialize();

        return executor;
    }
}
