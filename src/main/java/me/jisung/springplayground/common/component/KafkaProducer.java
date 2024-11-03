package me.jisung.springplayground.common.component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "KafkaProducer")
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * basic record produce
     * @param topic topic name
     * @param message produce message
     * */
    public void produce(String topic, String message) {
        produce(topic, null, null, message);
    }

    /**
     * hash key 지정하여 produce
     * @param topic topic name
     * @param key record hash key
     * @param message produce message
     * */
    public void produce(String topic, String key, String message) {
        produce(topic, null, key, message);
    }

    /**
     * hash key 및 partition number 지정하여 produce
     * @param topic topic name
     * @param partitionNo partition number
     * @param key record hash key
     * @param message produce message
     * */
    public void produce(String topic, Integer partitionNo, String key, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, partitionNo, key, message);

        try {
            SendResult<String, String> result = kafkaTemplate.send(record).get(10, TimeUnit.SECONDS);
            handleResult(result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            handleResult(e);
        }

        // asynchronous send - future는 비동기 연산에 대한 결과를 표현하는 객체이다.
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
        future.whenComplete(this::handleResult);
    }


    private void handleResult(SendResult<String, String> result) {
        handleResult(result, null);
    }

    private void handleResult(Throwable e) {
        handleResult(null, e);
    }

    private void handleResult(SendResult<String, String> result, Throwable e) {
        if (e != null) log.error("{}", e.toString());
        else log.info("{}", result);
    }
}