package com.practice.chatting.dto;

import com.practice.chatting.domain.chat.MessageType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageDto {
  private Long chatRoomId;
  private MessageType messageType;
  private String sender;
  private String content;
  private LocalDateTime createdAt;
}
