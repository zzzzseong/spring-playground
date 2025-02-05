package me.jisung.springplayground.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.chat.collection.ChatMessageCollection;
import me.jisung.springplayground.chat.dto.ChatMessageRequest;
import me.jisung.springplayground.chat.mapper.ChatMapper;
import me.jisung.springplayground.chat.repository.ChatMessageRepository;
import me.jisung.springplayground.common.component.KafkaProducer;
import me.jisung.springplayground.common.constant.KafkaConst;
import me.jisung.springplayground.common.util.JsonUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "MessageController")
public class MessageController {

    private final ChatMessageRepository chatMessageRepository;

    private final KafkaProducer kafkaProducer;
    private final SimpMessagingTemplate template;

    /* wss://{host}:{port}/app/chat 경로로 메시지 수신 */
    @MessageMapping("/chat")
    public void publishMessage(@RequestBody ChatMessageRequest request) {
        /* 발행 요청 데이터 프로듀싱 */
        ChatMessageCollection message = ChatMapper.toMessageCollection(request);
        kafkaProducer.produce(KafkaConst.TOPIC_NAME_CHAT, JsonUtil.toJson(message));

        /* MongoDB 채팅 데이터 저장 */
        chatMessageRepository.save(message);
    }

    @KafkaListener(topics = KafkaConst.TOPIC_NAME_CHAT)
    public void consumeMessage(ConsumerRecords<String, String> records) {
        try {
            for (ConsumerRecord<String, String> record : records) {
                String message = record.value();
                log.info("message consumed - topic: {}, message: {}", KafkaConst.TOPIC_NAME_CHAT, message);

                /* 채팅방 구독 사용자에게 메시지 전송 */
                ChatMessageCollection chatMessageRequestVo = JsonUtil.fromJson(record.value(), ChatMessageCollection.class);
                String stompTopic = "/topic/chat/" + chatMessageRequestVo.getRoomId();
                template.convertAndSend(stompTopic, message);
                log.info("message sent success to [{}]", stompTopic);
            }
        } catch (MessagingException e) {
            log.error("message send failed: {}", e.getMessage());
        }
    }
}