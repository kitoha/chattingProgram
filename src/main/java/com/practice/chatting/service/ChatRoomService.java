package com.practice.chatting.service;

import com.practice.chatting.converter.ChatRoomDtoConverter;
import com.practice.chatting.domain.chat.ChatMessage;
import com.practice.chatting.domain.chat.ChatRoom;
import com.practice.chatting.domain.chat.ChatRoomUser;
import com.practice.chatting.domain.chat.MessageType;
import com.practice.chatting.domain.chat.RoomType;
import com.practice.chatting.domain.user.User;
import com.practice.chatting.dto.ChatMessageDto;
import com.practice.chatting.dto.ChatRoomDto;
import com.practice.chatting.dto.GroupChatRoomRequestDto;
import com.practice.chatting.dto.ResponseDto;
import com.practice.chatting.formater.GroupNameFormatter;
import com.practice.chatting.repository.ChatRoomRepository;
import com.practice.chatting.repository.ChatRoomUserRepository;
import com.practice.chatting.repository.MessageRepository;
import com.practice.chatting.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
  private final ChatRoomRepository chatRoomRepository;
  private final ChatRoomUserRepository chatRoomUserRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChatRoomDtoConverter chatRoomDtoConverter;
  private final SimpMessagingTemplate messagingTemplate;
  private final ChatService chatService;

  @Transactional
  public ResponseDto<ChatRoomDto> getOrCreateOneToOneChatRoom(Long toUserId, String username){
    try {
      User user = userRepository.getByUsername(username);
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

  @Transactional
  public ResponseDto<ChatRoomDto> createGroupChatRoom(GroupChatRoomRequestDto requestDto, String creatorUsername){
    try {
      User creator = userRepository.getByUsername(creatorUsername);

      Set<Long> participantIdSet = new HashSet<>(requestDto.getParticipantIds());

      List<User> participants = participantIdSet.stream()
          .map(userRepository::getById)
          .collect(Collectors.toCollection(ArrayList::new));

      String groupName = GroupNameFormatter.formatGroupName(participants,requestDto.getGroupName());

      ChatRoom chatRoom = ChatRoom.builder()
          .roomType(RoomType.GROUP)
          .name(groupName)
          .build();

      chatRoom = chatRoomRepository.save(chatRoom);

      participants.add(creator);

      for (User participant : participants) {
        addUserToChatRoom(chatRoom, participant);
      }

      ChatRoomDto chatRoomDto = chatRoomDtoConverter.convertToChatRoomDto(chatRoom);

      return ResponseDto.success(HttpStatus.OK,"그룹 채팅방 생성에 성공하엿습니다.",chatRoomDto);
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 채팅방 생성에 실패하였습니다.");
    }
  }

  private ChatRoom createOneToOneChatRoom(Long toUserId, Long fromUserId) {
    User toUser = userRepository.getById(toUserId);
    User fromUser = userRepository.getById(fromUserId);

    ChatRoom chatRoom = ChatRoom.builder()
        .roomType(RoomType.ONE_TO_ONE)
        .name(toUser.getUsername() + "와의 채팅방")
        .build();

    chatRoom = chatRoomRepository.save(chatRoom);

    addUserToChatRoom(chatRoom, toUser);
    addUserToChatRoom(chatRoom, fromUser);

    return chatRoom;
  }

  @Transactional(readOnly = true)
  public ResponseDto<List<ChatMessageDto>> getChatMessage(Long chatRoomId){
    try {
      ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
          .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

      List<ChatMessageDto> messageDtos = messageRepository.findByChatRoom(chatRoom)
          .stream()
          .map(message -> ChatMessageDto.builder()
              .chatRoomId(chatRoom.getId())
              .messageType(message.getMessageType())
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
      User user = userRepository.getByUsername(username);
      List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findByUserAndLeftAtIsNull(user);
      List<ChatRoomDto> chatRoomDtoList = chatRoomUserList.stream()
          .map(ChatRoomUser::getChatRoom)
          .map(chatRoomDtoConverter::convertToChatRoomDto)
          .collect(Collectors.toList());
      
      return ResponseDto.success(HttpStatus.OK, "채팅방 목록 조회 성공", chatRoomDtoList);
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "채팅창 목록 조회 실패");
    }
  }

  @Transactional
  public ResponseDto<String> leaveChatRoom(Long chatRoomId, String username){
    try {
      User user = userRepository.getByUsername(username);
      ChatRoom chatRoom = chatRoomRepository.getById(chatRoomId);
      ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomAndUserAndLeftAtIsNull(
              chatRoom, user)
          .orElseThrow(() -> new IllegalStateException(
              "Active participation record not found for user: " + user.getUsername()));

      markUserAsLeft(chatRoomUser);

      if (isChatRoomEmpty(chatRoom)) {
        chatRoomUserRepository.deleteAllByChatRoom(chatRoom);
        chatRoomRepository.delete(chatRoom);
      }else{
        notifyUserLeft(chatRoom.getId(), username);
      }

      return ResponseDto.success(HttpStatus.OK, "채팅방 나가기 성공", null);
    }catch (Exception e) {
      return ResponseDto.failure(HttpStatus.BAD_REQUEST, "채팅방 나가기 실패: " + e.getMessage());
    }
  }

  private void markUserAsLeft(ChatRoomUser chatRoomUser) {
    chatRoomUser.setLeftAt(LocalDateTime.now());
    chatRoomUserRepository.save(chatRoomUser);
  }

  private boolean isChatRoomEmpty(ChatRoom chatRoom) {
    int activeCount = chatRoomUserRepository.countByChatRoomAndLeftAtIsNull(chatRoom);
    return activeCount == 0;
  }

  private void notifyUserLeft(Long chatRoomId, String username) {
    ChatMessage chatMessage = new ChatMessage(chatRoomId, MessageType.LEAVE,username,username + " 님이 채팅방을 나갔습니다.");
    chatService.sendChatMessage(chatMessage, username);
    messagingTemplate.convertAndSend("/topic/messages." + chatRoomId, chatMessage);
  }
}
