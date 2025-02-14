package com.practice.chatting.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
  private Long chatRoomId;
  private String sender;
  private String content;
  private String receiver;
}
