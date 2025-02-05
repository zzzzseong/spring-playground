package me.jisung.springplayground.chat.collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChatMessageCollection {

    @Id
    private String id;

    private String roomId;
    private Long userId;
    private String username;
    private String content;
    private String timestamp;
}