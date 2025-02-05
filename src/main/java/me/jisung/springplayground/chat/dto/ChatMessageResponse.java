package me.jisung.springplayground.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatMessageResponse {
    private Long userId;
    private String username;
    private String content;
    private String timestamp;
}
