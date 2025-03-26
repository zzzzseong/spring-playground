package me.jisung.springplayground.common.component;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void enqueue(String queue, String message) {
        rabbitTemplate.convertAndSend(queue, message);
    }
}
