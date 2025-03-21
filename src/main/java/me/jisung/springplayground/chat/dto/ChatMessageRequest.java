package me.jisung.springplayground.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequest {
    private String roomId;
    private Long userId;
    private String username;
    private String content;
    private String timestamp;
}