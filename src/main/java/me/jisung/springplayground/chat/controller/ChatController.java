package me.jisung.springplayground.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.chat.collection.ChatRoomCollection;
import me.jisung.springplayground.chat.dto.ChatRoomRequestVo;
import me.jisung.springplayground.chat.mapper.ChatMapper;
import me.jisung.springplayground.chat.repository.ChatRoomRepository;
import me.jisung.springplayground.common.exception.Api4xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.common.util.SecurityUtil;
import me.jisung.springplayground.user.entity.UserEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j(topic = "ChatController")
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅방 생성
     * @param chatRoomRequestVo 채팅방 생성 요청 VO
     * */
    @PostMapping("/room")
    public void createChatRoom(@RequestBody ChatRoomRequestVo chatRoomRequestVo) {
        UserEntity user = SecurityUtil.getAuthenticatedUser();
        ChatRoomCollection chatRoom = ChatMapper.toChatCollection(user, chatRoomRequestVo);
        chatRoomRepository.save(chatRoom);
    }

    /**
     * 채팅방에 사용자 추가
     * @param roomId 사용자를 추가할 채팅방 ID
     * */
    @PatchMapping("/room/{roomId}")
    public void updateChatRoom(@PathVariable String roomId) {
        ChatRoomCollection chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
            () -> new ApiException(Api4xxErrorCode.ENTITY_NOT_FOUND, "채팅방 정보를 찾을 수 없습니다.")
        );

        /* 채팅방 사용자 목록 조회 및 예외처리 */
        List<Long> userIds = chatRoom.getUserIds();
        Long userId = SecurityUtil.getAuthenticatedUser().getId();
        if(userIds.contains(userId)) throw new ApiException(Api4xxErrorCode.ALREADY_EXISTS_ENTITY, "이미 채팅방에 참여한 사용자입니다.");

        /* 채팅방 사용자 목록에 사용자 ID 추가 및 저장 */
        chatRoom.getUserIds().add(userId);
        chatRoomRepository.save(chatRoom);
    }
}
