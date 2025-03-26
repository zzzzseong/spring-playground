package me.jisung.springplayground.common.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "RMQConsumer")
public class RabbitMQConsumer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Consuming message using rabbitListener annotation
     * @param message consumed message
     * */
    @RabbitListener(queues = "playground")
    public void consume(String message) {
        log.info("Message consumed from {}: {}", "playground", message);
    }

    /**
     * Consuming message manually using rabbitTemplate
     * */
    public void consumeManually() {
        String message = String.valueOf(rabbitTemplate.receiveAndConvert("playground"));

        log.info("Message consumed manually from {}: {}", "playground", message);
    }
}
