package me.jisung.springplayground.common.config.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka_bootstrap_servers}")
    private String bootstrapServers;

    @Value("${kafka_consumer_group}")
    private String consumerGroup;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        // fetcher를 이용해 batch로 record를 읽어들이기 위한 설정 (ConsumerRecords 사용 가능)
        factory.setBatchListener(true);

        // RebalanceListener 등록
        factory.getContainerProperties().setConsumerRebalanceListener(new RebalanceListener());

        return factory;
    }

    private ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        // kafka cluster address setting
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // string->object deserializer setting
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // consumer group setting
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);

        // earliest : 가장 처음부터 읽기, latest : 가장 마지막부터 읽기
        // consumer offset 을 사용할 수 없는 상태이거나 offset 정보를 찾을 수 없을떄의 option
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // auto commit(true), manual commit(false)
        /* props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); */

        // record를 읽어들이는 최소 byte size setting (default: 1byte)
        /* props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);*/

        // 최소 byte size 만큼의 record가 fetcher에 적재되기까지 기다리는 최대 대기 시간(milli-seconds)
        /* props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1024);*/

        // 토픽 자동 생성 설정
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);

        // consumer offset commit interval setting (default: 5000ms)
        /* props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3000);*/

        // transaction consumer setting
        /* props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");*/

        return new DefaultKafkaConsumerFactory<>(props);
    }
}