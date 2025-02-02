package com.practice.chatting.controller;

import com.practice.chatting.domain.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

  @MessageMapping("/chat")
  @SendTo("/topic/messages")
  public ChatMessage send(ChatMessage message) {
    return message;
  }
}
