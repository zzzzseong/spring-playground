package me.jisung.springplayground.chat.repository;

import me.jisung.springplayground.chat.collection.ChatRoomCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoomCollection, String> {

}
