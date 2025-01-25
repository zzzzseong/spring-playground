package me.jisung.springplayground.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class StompMessageDto {
    private Long roomId;
    private Long userId;
    private String username;
    private String content;

    @Setter
    private String timestamp;
}