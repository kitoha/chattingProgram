package com.practice.chatting.service;

import com.practice.chatting.domain.chat.ChatMessage;
import com.practice.chatting.domain.chat.ChatRoom;
import com.practice.chatting.domain.chat.Message;
import com.practice.chatting.domain.user.User;
import com.practice.chatting.repository.MessageRepository;
import com.practice.chatting.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  @Transactional
  public ChatMessage sendChatMessage(ChatMessage chatMessage, String username){
    User sender = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Message message = Message.create(sender,
        new ChatRoom(),
        chatMessage.getContent());

    messageRepository.save(message);
    return chatMessage;
  }

}
