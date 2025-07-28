package me.jisung.springplayground.common.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka_bootstrap_servers}")
    private String bootstrapServers;

    private final String[] acks = {"0", "1", "all"};

    private static final int BATCH_SIZE = 10;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }

    private ProducerFactory<String, String> kafkaProducerFactory() {
        Map<String, Object> props = new HashMap<>();

        // kafka cluster address setting
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // serializer setting
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,   StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 멱등성 프로듀서 세팅 - idempotence producer setting (exactly once)
        // 카프카 3.x.x 부터는 default 값이 true 이다.
        /* props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);*/

        // 트랜젝션 프로듀서 세팅 - transaction producer setting
        /* props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, UUID.randomUUID());*/

        // batch size setting
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE);

        // producer acknowledge setting
        props.put(ProducerConfig.ACKS_CONFIG, acks[1]);

        // register custom partitioner
        /* props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class);*/

        return new DefaultKafkaProducerFactory<>(props);
    }
}
