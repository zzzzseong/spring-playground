package me.jisung.springplayground.chat.mapper;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jisung.springplayground.chat.collection.ChatMessageCollection;
import me.jisung.springplayground.chat.collection.ChatRoomCollection;
import me.jisung.springplayground.chat.dto.ChatMessageRequest;
import me.jisung.springplayground.chat.dto.ChatMessageResponse;
import me.jisung.springplayground.chat.dto.ChatRoomRequest;
import me.jisung.springplayground.common.util.DateUtil;
import me.jisung.springplayground.user.entity.UserEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMapper {

    public static ChatRoomCollection toChatCollection(UserEntity user, ChatRoomRequest request) {
        String now = DateUtil.now(DateUtil.DATE_TIME_FORMAT);
        return ChatRoomCollection.builder()
            .name(request.getChatRoomName())
            .userIds(List.of(user.getId()))
            .createdAt(now)
            .build();
    }
    public static ChatMessageCollection toMessageCollection(ChatMessageRequest request) {
        String now = DateUtil.now(DateUtil.DATE_TIME_FORMAT);
        return ChatMessageCollection.builder()
            .roomId(request.getRoomId())
            .userId(request.getUserId())
            .username(request.getUsername())
            .content(request.getContent())
            .timestamp(now)
            .build();
    }

    public static ChatMessageResponse toMessageResponse(ChatMessageCollection message) {
        return ChatMessageResponse.builder()
            .userId(message.getUserId())
            .username(message.getUsername())
            .content(message.getContent())
            .timestamp(message.getTimestamp())
            .build();
    }

}
