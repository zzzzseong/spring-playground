package me.jisung.springplayground.common.config.kafka;

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

/**
 * 리밸런스 이벤트를 처리하기 위한 리스너 클래스
 * */
@Slf4j(topic = "RebalanceListener")
public class RebalanceListener implements ConsumerRebalanceListener {

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        // 리밸런스가 시작되기 직전에 호출되는 메서드
        // 보통 해당 메서드를 이용해 마지막으로 처리한 레코드에 대한 커밋을 진행한다.
        log.info("onPartitionsRevoked: {}", partitions);
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        // 리밸런스가 끝난 뒤 파티션 할당이 완려되면 호출되는 메서드
        log.info("onPartitionsAssigned: {}", partitions);
    }
}
