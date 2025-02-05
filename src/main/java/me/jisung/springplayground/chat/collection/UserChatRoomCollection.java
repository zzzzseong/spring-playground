package me.jisung.springplayground.chat.collection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "user_chat_rooms")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserChatRoomCollection {

    @Id
    private String id;

    private ChatRoomIds[] chatRoomIds;

    @Getter
    public static class ChatRoomIds {
        private Long roomId;
        private String joinedAt;
    }
}