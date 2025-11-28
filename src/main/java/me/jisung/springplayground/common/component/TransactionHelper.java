package me.jisung.springplayground.common.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 트랜잭션 처리를 위한 헬퍼 클래스입니다.
 * <p>
 * 내부적으로 {@link TransactionTemplate}을 초기화하여 재사용하며,
 * 트랜잭션 내 로직 실행 시 발생할 수 있는 주요 예외를 일관되게 로깅합니다.
 * </p>
 *
 * <pre>{@code
 * @Service
 * public class SomeService {
 *
 *     private final TransactionHelper transactionHelper;
 *
 *     public void someTransactionalOperation() {
 *         transactionHelper.execute(new TransactionCallbackWithoutResult() {
 *             @Override
 *             protected void doInTransactionWithoutResult(TransactionStatus status) {
 *                 // 트랜잭션 내 로직
 *             }
 *         });
 *     }
 * }
 * }</pre>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionHelper {

    private final PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void init() {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    /**
     * 트랜잭션을 시작하고, 주어진 콜백 로직을 트랜잭션 내에서 실행합니다.
     * <p>
     * 데이터 무결성 위반이나 잘못된 데이터 접근 등의 예외가 발생할 경우, 해당 내용을 로깅합니다.
     * </p>
     *
     * @param callback 트랜잭션 내에서 실행할 작업
     */
    public void execute(TransactionCallbackWithoutResult callback) {
        try {
            transactionTemplate.execute(callback);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation occurred during transaction execution.", e);
        } catch (InvalidDataAccessResourceUsageException e) {
            log.error("Invalid data access or constraint violation during transaction execution.", e);
        } catch (Exception e) {
            log.error("Unexpected exception occurred during transaction execution.", e);
        }
    }
}
