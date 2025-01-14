package me.jisung.springplayground.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.component.KafkaProducer;
import me.jisung.springplayground.common.json.vo.StompMessageVo;
import me.jisung.springplayground.common.util.DateUtil;
import me.jisung.springplayground.common.util.JsonUtil;
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

    private final SimpMessagingTemplate template;

    /* wss://{host}:{port}/app/message 경로로 메시지 수신 */
    @MessageMapping("/message")
    public void publishMessage(StompMessageVo message) {
        log.info("message: {}", JsonUtil.toJson(message));
        kafkaProducer.produce("chat", JsonUtil.toJson(message));
    }

    @KafkaListener(topics = "chat")
    public void consumeMessage(String message) {
        String now = DateUtil.now(DateUtil.DATE_TIME_FORMAT);
        log.info("consumed message: {}, timestamp: {}", message, now);

        try {
            template.convertAndSend("/topic/chat", message);
            log.info("message sent success to /topic/chat");

            //TODO - 여기서 도착한 메시지를 몽고 db에 저장
        } catch (MessagingException e) {
            log.error("failed to send message: {}", e.getMessage());
        }
    }
}