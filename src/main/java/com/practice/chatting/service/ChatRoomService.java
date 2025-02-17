package com.practice.chatting.service;

import com.practice.chatting.domain.chat.ChatRoom;
import com.practice.chatting.domain.chat.ChatRoomUser;
import com.practice.chatting.domain.chat.RoomType;
import com.practice.chatting.domain.user.User;
import com.practice.chatting.dto.ChatMessageDto;
import com.practice.chatting.dto.ChatRoomDto;
import com.practice.chatting.dto.ResponseDto;
import com.practice.chatting.repository.ChatRoomRepository;
import com.practice.chatting.repository.ChatRoomUserRepository;
import com.practice.chatting.repository.MessageRepository;
import com.practice.chatting.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
  private final ChatRoomRepository chatRoomRepository;
  private final ChatRoomUserRepository chatRoomUserRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  @Transactional
  public ResponseDto<ChatRoomDto> getOrCreateOneToOneChatRoom(Long toUserId, String username){
    try {
      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found"));
      Long fromUserId = user.getId();
      Optional<ChatRoom> existingRoom = chatRoomRepository.findOneToOneChatRoom(toUserId,
          fromUserId, RoomType.ONE_TO_ONE);
      ChatRoom chatRoom = existingRoom.orElseGet(
          () -> createOneToOneChatRoom(toUserId, fromUserId));
      ChatRoomDto chatRoomDto = ChatRoomDto.builder()
          .id(chatRoom.getId())
          .roomType(chatRoom.getRoomType())
          .name(chatRoom.getName())
          .build();
      return ResponseDto.success(HttpStatus.OK, "채팅방 조회 및 생성에 완료하였습니다.", chatRoomDto);
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 조회 및 생성에 실패하였습니다.");
    }
  }

  private ChatRoom createOneToOneChatRoom(Long toUserId, Long fromUserId) {
    ChatRoom chatRoom = ChatRoom.builder()
        .roomType(RoomType.ONE_TO_ONE)
        .name(null)
        .build();

    chatRoom = chatRoomRepository.save(chatRoom);

    User toUser = userRepository.findById(toUserId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    User fromUser = userRepository.findById(fromUserId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    addUserToChatRoom(chatRoom, toUser);
    addUserToChatRoom(chatRoom, fromUser);

    return chatRoom;
  }

  public ResponseDto<List<ChatMessageDto>> getChatMessage(Long chatRoomId){
    try {
      ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
          .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

      List<ChatMessageDto> messageDtos = messageRepository.findByChatRoom(chatRoom)
          .stream()
          .map(message -> ChatMessageDto.builder()
              .chatRoomId(chatRoom.getId())
              .sender(message.getSender().getUsername())
              .content(message.getContent())
              .createdAt(message.getCreatedAt())
              .build())
          .toList();

      return ResponseDto.success(HttpStatus.OK, "메세지 목록 조회 성공하였습니다.", messageDtos);
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "메세지 목록 조회에 실패하였습니다.");
    }
  }

  private ChatRoomUser addUserToChatRoom(ChatRoom chatRoom, User user){
    ChatRoomUser chatRoomUser = ChatRoomUser.builder()
        .chatRoom(chatRoom)
        .user(user)
        .joinedAt(LocalDateTime.now())
        .leftAt(null)
        .build();
    return chatRoomUserRepository.save(chatRoomUser);
  }

  @Transactional(readOnly = true)
  public ResponseDto<List<ChatRoomDto>> getChatRoomsForUser(String username){
    try {
      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found"));
      List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findByUserAndLeftAtIsNull(user);
      List<ChatRoomDto> chatRoomDtoList = chatRoomUserList.stream()
          .map(ChatRoomUser::getChatRoom)
          .map(this::convertToChatRoomDto)
          .collect(Collectors.toList());
      
      return ResponseDto.success(HttpStatus.OK, "채팅방 목록 조회 성공", chatRoomDtoList);
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "채팅창 목록 조회 실패");
    }
  }

  private ChatRoomDto convertToChatRoomDto(ChatRoom chatRoom){
    ChatMessageDto lastMessageDto = getLastMessageDto(chatRoom);
    int participantCount = getParticipantCount(chatRoom);

    return ChatRoomDto.builder()
        .id(chatRoom.getId())
        .roomType(chatRoom.getRoomType())
        .name(chatRoom.getName())
        .lastMessage(lastMessageDto)
        .participantCount(participantCount)
        .build();
  }

  private ChatMessageDto getLastMessageDto(ChatRoom chatRoom){
    return messageRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom)
        .map(message -> ChatMessageDto.builder()
            .chatRoomId(chatRoom.getId())
            .sender(message.getSender().getUsername())
            .content(message.getContent())
            .createdAt(message.getCreatedAt())
            .build())
        .orElse(null);
  }

  private int getParticipantCount(ChatRoom chatRoom){
    return chatRoomUserRepository.countByChatRoomAndLeftAtIsNull(chatRoom);
  }
}
