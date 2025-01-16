package me.jisung.springplayground.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.component.KafkaProducer;
import me.jisung.springplayground.common.component.MongoHelper;
import me.jisung.springplayground.common.json.vo.StompMessageVo;
import me.jisung.springplayground.common.util.DateUtil;
import me.jisung.springplayground.common.util.JsonUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "MessageController")
public class StompMessageController {

    private final KafkaProducer kafkaProducer;
    private final MongoHelper mongo;

    private final SimpMessagingTemplate template;

    /* wss://{host}:{port}/app/message 경로로 메시지 수신 */
    @MessageMapping("/message")
    public void publishMessage(StompMessageVo message) {
        log.info("message: {}", JsonUtil.toJson(message));
        kafkaProducer.produce("chat", JsonUtil.toJson(message));
    }

    @KafkaListener(topics = "chat")
    public void consumeMessage(ConsumerRecords<String, String> records) {
        String now = DateUtil.now(DateUtil.DATE_TIME_FORMAT);

        try {
            for (ConsumerRecord<String, String> record : records) {
                String message = record.value();
                log.info("consumed message: {}, timestamp: {}", message, now);

                template.convertAndSend("/topic/chat", message);
                log.info("message sent success to /topic/chat");

                /* save log mongodb data */
                 mongo.insertOne("chat", message);
            }
        } catch (MessagingException e) {
            log.error("failed to send message: {}", e.getMessage());
        }
    }
}