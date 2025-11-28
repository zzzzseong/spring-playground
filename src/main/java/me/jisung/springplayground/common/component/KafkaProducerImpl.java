package me.jisung.springplayground.common.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produce(String topic, String message) {
        produce(topic, null, null, message);
    }

    public void produce(String topic, String key, String message) {
        produce(topic, null, key, message);
    }

    /**
     * Synchronously produces a message to a Kafka topic.
     *
     * <p>This method sends a message to the specified Kafka topic using the given partition,
     * key, and message value. It waits for the send operation to complete within a timeout
     * and then passes the result or any exception to the result handler.
     *
     * @param topic       the name of the Kafka topic
     * @param partitionNo the partition number to send the message to
     * @param key         the record key (used for partitioning)
     * @param message     the message content to send
     */
    @Override
    public void produce(String topic, Integer partitionNo, String key, String message) {
        ProducerRecord<String, String> rcd = new ProducerRecord<>(topic, partitionNo, key, message);

        try {
            SendResult<String, String> result = kafkaTemplate.send(rcd).get(10, TimeUnit.SECONDS);
            handleResult(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            handleResult(e);
        } catch (ExecutionException | TimeoutException e) {
            handleResult(e);
        }
    }


    public void produceAsync(String topic, String message) {
        produceAsync(topic, null, null, message);
    }

    public void produceAsync(String topic, String key, String message) {
        produceAsync(topic, null, key, message);
    }

    /**
     * Asynchronously produces a message to a Kafka topic.
     *
     * <p>This method sends a message to the specified Kafka topic using the given partition,
     * key, and message value. It uses {@link CompletableFuture} to handle the result asynchronously.
     * When the send operation completes (success or failure), the result is passed to the result handler.
     *
     * @param topic       the name of the Kafka topic
     * @param partitionNo the partition number to send the message to
     * @param key         the record key (used for partitioning)
     * @param message     the message content to send
     */
    @Override
    public void produceAsync(String topic, Integer partitionNo, String key, String message) {
        ProducerRecord<String, String> rcd = new ProducerRecord<>(topic, partitionNo, key, message);

        // CompletableFuture: represents the result of an asynchronous computation
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(rcd);
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
