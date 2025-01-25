package me.jisung.springplayground.chat.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * MongoHelper is a component that provides MongoDB operations.
 * */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "MongoHelper")
public class MongoHelper {

    private final MongoTemplate mongoTemplate;

    /**
     * Insert a message into the specified collection.
     * @param collectionName Collection name
     * @param message Message to insert
     * */
    public void insertOne(String collectionName, String message) {
        mongoTemplate.insert(message, collectionName);
    }

    // TODO - 채팅방에 사용자 입장했을 시 메서드 구현
    public void update(Long roomId, Long userId) {

        // 1. 채팅방을 찾는 조건
        Query query = new Query(Criteria.where("_id").is(roomId));

        // 2. userIds 배열에 값을 추가 (중복 방지)
        Update update = new Update().addToSet("userIds", userId);

        // 3. 컬렉션 업데이트 실행
        mongoTemplate.updateFirst(query, update, "chat_rooms");
    }
}