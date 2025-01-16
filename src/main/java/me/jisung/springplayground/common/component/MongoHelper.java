package me.jisung.springplayground.common.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
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

}