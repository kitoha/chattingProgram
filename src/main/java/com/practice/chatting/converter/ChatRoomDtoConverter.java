package com.practice.chatting.converter;

import com.practice.chatting.domain.chat.ChatRoom;
import com.practice.chatting.dto.chat.ChatMessageDto;
import com.practice.chatting.dto.chat.ChatRoomDto;
import com.practice.chatting.repository.chat.ChatRoomUserRepository;
import com.practice.chatting.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomDtoConverter {
  private final ChatRoomUserRepository chatRoomUserRepository;
  private final MessageRepository messageRepository;

  public ChatRoomDto convertToChatRoomDto(ChatRoom chatRoom){
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
