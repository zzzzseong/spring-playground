package me.jisung.springplayground.chat.mapper;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jisung.springplayground.chat.collection.ChatMessageCollection;
import me.jisung.springplayground.chat.collection.ChatRoomCollection;
import me.jisung.springplayground.chat.dto.ChatMessageRequestVo;
import me.jisung.springplayground.chat.dto.ChatRoomRequestVo;
import me.jisung.springplayground.common.util.DateUtil;
import me.jisung.springplayground.user.entity.UserEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMapper {

    public static ChatRoomCollection toChatCollection(UserEntity user, ChatRoomRequestVo request) {
        String now = DateUtil.now(DateUtil.DATE_TIME_FORMAT);
        return ChatRoomCollection.builder()
            .name(request.getChatRoomName())
            .userIds(List.of(user.getId()))
            .createdAt(now)
            .build();
    }

    public static ChatMessageCollection toMessageCollection(ChatMessageRequestVo request) {
        String now = DateUtil.now(DateUtil.DATE_TIME_FORMAT);
        return ChatMessageCollection.builder()
            .roomId(request.getRoomId())
            .userId(request.getUserId())
            .username(request.getUsername())
            .content(request.getContent())
            .timestamp(now)
            .build();
    }

}
