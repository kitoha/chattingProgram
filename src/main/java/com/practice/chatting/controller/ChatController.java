package com.practice.chatting.controller;

import com.practice.chatting.domain.chat.ChatMessage;
import com.practice.chatting.service.ChatService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  @MessageMapping("/chat.{chatRoomId}")
  @SendTo("/topic/messages.{chatRoomId}")
  public ChatMessage send(ChatMessage message,@DestinationVariable Long chatRoomId, Principal principal) {
    return chatService.sendChatMessage(message, principal.getName());
  }
}
