package me.jisung.springplayground.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.chat.component.MongoHelper;
import me.jisung.springplayground.chat.dto.StompMessageDto;
import me.jisung.springplayground.common.component.KafkaProducer;
import me.jisung.springplayground.common.constant.KafkaConst;
import me.jisung.springplayground.common.util.DateUtil;
import me.jisung.springplayground.common.util.JsonUtil;
import me.jisung.springplayground.common.util.SecurityUtil;
import me.jisung.springplayground.user.entity.UserEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "MessageController")
public class StompMessageController {

    private final KafkaProducer kafkaProducer;
    private final MongoHelper mongo;

    private final SimpMessagingTemplate template;

    @Getter
    @AllArgsConstructor
    public static class ChatRoom {
        private String name;
        private Long[] userIds;
        private String createdAt;
    }

    /**
     * 채팅방 생성
     * {
     *     "_id": 1255,
     *     "name": "개발자 모임",
     *     "userIds": [1, 10],
     *     "createdAt": "2024-03-01 12:34:56"
     * }
     * */
    @PostMapping("/chat/room")
    public void createChatRoom() {
        UserEntity user = SecurityUtil.getAuthenticatedUser();
        ChatRoom chatRoom = new ChatRoom("개발자 모임", new Long[]{user.getId()}, DateUtil.now(DateUtil.DATE_TIME_FORMAT));
        mongo.insertOne("chat_room", JsonUtil.toJson(chatRoom));
    }

//    @MessageMapping("/chat/enter")
//    public void enterChatRoom() {
//        UserEntity user = SecurityUtil.getAuthenticatedUser();
//
//
//    }

    /* wss://{host}:{port}/app/chat 경로로 메시지 수신 */
    @MessageMapping("/chat")
    public void publishMessage(@RequestBody StompMessageDto stompMessageDto) {
        /* 발행 요청 데이터 프로듀싱 */
        stompMessageDto.setTimestamp(DateUtil.now(DateUtil.DATE_TIME_FORMAT));
        String message = JsonUtil.toJson(stompMessageDto);
        kafkaProducer.produce(KafkaConst.TOPIC_NAME_CHAT, message);

        /* MongoDB 채팅 데이터 저장 */
        mongo.insertOne("chat_room_" + stompMessageDto.getRoomId(), message);
    }

    @KafkaListener(topics = KafkaConst.TOPIC_NAME_CHAT)
    public void consumeMessage(ConsumerRecords<String, String> records) {
        try {
            for (ConsumerRecord<String, String> record : records) {
                String message = record.value();
                log.info("message consumed - topic: {}, message: {}", KafkaConst.TOPIC_NAME_CHAT, message);

                /* 채팅방 구독 사용자에게 메시지 전송 */
                StompMessageDto stompMessageDto = JsonUtil.fromJson(record.value(), StompMessageDto.class);
                String stompTopic = "/topic/chat/" + stompMessageDto.getRoomId();
                template.convertAndSend(stompTopic, message);
                log.info("message sent success to [{}]", stompTopic);
            }
        } catch (MessagingException e) {
            log.error("message send failed: {}", e.getMessage());
        }
    }
}