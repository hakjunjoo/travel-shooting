package com.sparta.travelshooting.chat.controller;

import com.sparta.travelshooting.chat.dto.ChatMessageResponseDto;
import com.sparta.travelshooting.chat.dto.ChatRoomRequestDto;
import com.sparta.travelshooting.chat.service.ChatRoomService;
import com.sparta.travelshooting.common.entity.ApiResponseDto;
import com.sparta.travelshooting.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
@Tag(name = "채팅방 관리 API")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 개설")
    @PostMapping
    public ApiResponseDto createChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        return chatRoomService.createChatRoom(chatRoomRequestDto.getRoomName());
    }

    @Operation(summary = "채팅방 삭제")
    @DeleteMapping("/{chatRoomId}")
    public ApiResponseDto deleteChatRoom(@PathVariable Long chatRoomId) {
        return chatRoomService.deleteChatRoom(chatRoomId);
    }

    @Operation(summary = "채팅방 입장")
    @PostMapping("/{chatRoomId}/join")
    public ApiResponseDto joinChatRoom(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.joinChatRoom(userDetails.getUser().getId(), chatRoomId); //userId는 User 서비스 merge 하기 전 임시
    }

    @Operation(summary = "채팅방 나가기")
    @PostMapping("/{chatRoomId}/leave")
    public ApiResponseDto leaveChatRoom(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.leaveChatRoom(userDetails.getUser().getId(), chatRoomId); //userId는 User 서비스 merge 하기 전 임시
    }

    @Operation(summary = "채팅방 채팅 내역 전체 불러오기")
    @GetMapping("/{chatRoomId}")
    public List<ChatMessageResponseDto> getChatRoomChatMessage(@PathVariable Long chatRoomId) {
        return chatRoomService.getChatRoomChatMessage(chatRoomId);
    }

    @Operation(summary = "채팅방 채팅 내역 페이징으로 불러오기")
    @GetMapping("/{chatRoomId}/paging")
    public List<ChatMessageResponseDto> getChatRoomChatMessagePaging(@PathVariable Long chatRoomId, Pageable pageable) {
        return chatRoomService.getChatRoomChatMessagePaging(chatRoomId, pageable);
    }
}
