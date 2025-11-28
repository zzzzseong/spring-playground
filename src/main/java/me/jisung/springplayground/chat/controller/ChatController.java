package me.jisung.springplayground.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.chat.collection.ChatMessageCollection;
import me.jisung.springplayground.chat.collection.ChatRoomCollection;
import me.jisung.springplayground.chat.dto.ChatMessageResponse;
import me.jisung.springplayground.chat.dto.ChatRoomRequest;
import me.jisung.springplayground.chat.mapper.ChatMapper;
import me.jisung.springplayground.chat.repository.ChatMessageRepository;
import me.jisung.springplayground.chat.repository.ChatRoomRepository;
import me.jisung.springplayground.common.entity.ApiResponse;
import me.jisung.springplayground.common.exception.Api4xxErrorCode;
import me.jisung.springplayground.common.exception.ApiException;
import me.jisung.springplayground.common.util.SecurityUtil;
import me.jisung.springplayground.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final SimpMessagingTemplate template;

    /**
     * 채팅방 생성
     * @param chatRoomRequest 채팅방 생성 요청 VO
     * */
    @PostMapping("/room")
    public ApiResponse<Void> createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        UserEntity user = SecurityUtil.getAuthenticatedUser();
        ChatRoomCollection chatRoom = ChatMapper.toChatCollection(user, chatRoomRequest);
        chatRoomRepository.save(chatRoom);
        return success();
    }

    /**
     * 채팅방에 사용자 추가
     * @param roomId 사용자를 추가할 채팅방 ID
     * */
    @PatchMapping("/room/{roomId}")
    public ApiResponse<Void> updateChatRoom(@PathVariable String roomId) {
        ChatRoomCollection chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
            () -> new ApiException(Api4xxErrorCode.ENTITY_NOT_FOUND, "채팅방 정보를 찾을 수 없습니다.")
        );

        UserEntity user = SecurityUtil.getAuthenticatedUser();
        Long userId = user.getId();

        /* 채팅방 사용자 목록 조회 및 예외처리 */
        List<Long> userIds = chatRoom.getUserIds();
        if(userIds.contains(userId)) throw new ApiException(Api4xxErrorCode.ALREADY_EXISTS_ENTITY, "이미 채팅방에 참여한 사용자입니다.");

        /* 채팅방 사용자 목록에 사용자 ID 추가 및 저장 */
        chatRoom.getUserIds().add(userId);
        chatRoomRepository.save(chatRoom);

        String message = user.getName() + "님이 채팅방에 참여했습니다.";
        template.convertAndSend("/topic/chat/" + chatRoom.getId(), message);
        return success();
    }

    /**
     * 채팅방 메시지 목록 조회
     * */
    @GetMapping("/room/{roomId}/message")
    public ApiResponse<Page<ChatMessageResponse>> readChatMessages(@PathVariable String roomId, Pageable pageable) {
        Page<ChatMessageCollection> messages = chatMessageRepository.findAllByRoomId(pageable, roomId);
        return success(messages.map(ChatMapper::toMessageResponse));
    }
}
