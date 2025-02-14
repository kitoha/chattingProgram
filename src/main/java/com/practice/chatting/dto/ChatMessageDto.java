package com.practice.chatting.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageDto {
  private Long chatRoomId;
  private String sender;
  private String content;
  private LocalDateTime createdAt;
}
