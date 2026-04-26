package me.jisung.springplayground.common.controller;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.entity.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/virtual-thread-sample")
public class VirtualThreadSampleController {

    // Semaphore 패턴:
    // - permit(허가증) 개수만큼만 동시에 임계 구역(실제 작업) 진입 가능
    // - acquire()는 permit이 없으면 대기
    // - 작업 종료 후 release()로 permit 반환
    // - 결과적으로 "스레드 수 제한"이 아니라 "동시 실행 작업 수 제한"을 구현
    private final Semaphore semaphore = new Semaphore(3);

    // Executors.newVirtualThreadPerTaskExecutor()가 주입된 실행기
    // submit()마다 가상 스레드가 할당되지만, 실제 동시 수행은 semaphore가 제어
    private final ExecutorService virtualTaskExecutor;

    @GetMapping("/execute")
    public ApiResponse<Void> execute() throws Exception {
        int taskCount = 5; // 작업 사이즈
        long sleepMs = 300L;

        List<Future<Map<String, Object>>> futures = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            final int taskNo = i + 1;

            // 1) 각 작업을 가상 스레드 executor에 제출
            futures.add(virtualTaskExecutor.submit(() -> {

                Map<String, Object> taskResult = new LinkedHashMap<>();
                boolean acquired = false;

                try {
                    // 2) permit이 없으면 여기서 대기 -> permit 획득 후 작업 시작
                    semaphore.acquire();
                    acquired = true;

                    // 3) 실제 작업 영역(예시로 sleep)
                    Thread.sleep(sleepMs);
                    taskResult.put("taskNo", taskNo);
                    taskResult.put("status", "completed");
                    taskResult.put("threadName", Thread.currentThread().getName());
                    taskResult.put("isVirtual", Thread.currentThread().isVirtual());

                    log.info("virtual task completed. taskNo={}, thread={}, isVirtual={}", taskNo, Thread.currentThread().getName(), Thread.currentThread().isVirtual());
                    return taskResult;
                } catch (InterruptedException e) {
                    // 대기/수행 중 인터럽트 시 인터럽트 상태 복구 후 결과 반환
                    Thread.currentThread().interrupt();
                    taskResult.put("taskNo", taskNo);
                    taskResult.put("status", "interrupted");
                    taskResult.put("message", "task interrupted while waiting semaphore");
                    return taskResult;
                } finally {
                    // 4) permit을 획득한 작업만 반환 (누수 방지)
                    if (acquired) {
                        semaphore.release();
                    }
                }
            }));
        }

        // 5) 모든 작업의 완료를 기다림 (Future.get() = WaitGroup과 유사한 합류 지점)
        for (Future<Map<String, Object>> future : futures) log.info("{}", future.get());

        return success();
    }
}
