package me.jisung.springplayground.chat.repository;

import me.jisung.springplayground.chat.collection.ChatMessageCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessageCollection, String> {
    Page<ChatMessageCollection> findAllByRoomId(Pageable pageable, String roomId);
}
