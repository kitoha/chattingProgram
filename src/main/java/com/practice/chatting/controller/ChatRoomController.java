package com.practice.chatting.controller;

import com.practice.chatting.dto.ChatMessageDto;
import com.practice.chatting.dto.ChatRoomDto;
import com.practice.chatting.dto.ResponseDto;
import com.practice.chatting.repository.MessageRepository;
import com.practice.chatting.service.ChatRoomService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final MessageRepository messageRepository;

  @GetMapping("")
  public ResponseEntity<ResponseDto<List<ChatRoomDto>>> getUserChatRooms(Principal principal){
    ResponseDto<List<ChatRoomDto>> responseDto = chatRoomService.getChatRoomsForUser(principal.getName());
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }

  @GetMapping("/{chatRoomId}/messages")
  public ResponseEntity<ResponseDto<List<ChatMessageDto>>> getChatMessages(@PathVariable Long chatRoomId){
    ResponseDto<List<ChatMessageDto>> responseDto = chatRoomService.getChatMessage(chatRoomId);
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }

  @PostMapping("/one-to-one")
  public ResponseEntity<ResponseDto<ChatRoomDto>> getOrCreateOneToOneChatRoom(Principal principal,@RequestBody Long toUserId){
    ResponseDto<ChatRoomDto> responseDto = chatRoomService.getOrCreateOneToOneChatRoom(toUserId, principal.getName());
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }

}
