package me.jisung.springplayground.common.config.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka_bootstrap_servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }

    private ProducerFactory<String, String> kafkaProducerFactory() {
        Map<String, Object> props = new HashMap<>();

        // kafka cluster address setting
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // object->string serializer setting
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 멱등성 프로듀서 세팅 - idempotence producer setting (exactly once)
        /* props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);*/

        // 트랜젝션 프로듀서 세팅 - transaction producer setting
        /* props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, UUID.randomUUID());*/

        // producer acknowledge setting
        props.put(ProducerConfig.ACKS_CONFIG, "1");

        // register custom partitioner
        /* props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class);*/

        return new DefaultKafkaProducerFactory<>(props);
    }
}