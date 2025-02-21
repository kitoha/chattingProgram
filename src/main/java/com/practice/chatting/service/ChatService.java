package com.practice.chatting.service;

import com.practice.chatting.domain.chat.ChatMessage;
import com.practice.chatting.domain.chat.ChatRoom;
import com.practice.chatting.domain.chat.Message;
import com.practice.chatting.domain.user.User;
import com.practice.chatting.repository.ChatRoomRepository;
import com.practice.chatting.repository.MessageRepository;
import com.practice.chatting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChatRoomRepository chatRoomRepository;

  @Transactional
  public ChatMessage sendChatMessage(ChatMessage chatMessage, String username){
    User sender = userRepository.getByUsername(username);

    ChatRoom chatRoom = chatRoomRepository.getById(chatMessage.getChatRoomId());

    Message message = Message.builder()
        .chatRoom(chatRoom)
        .messageType(chatMessage.getMessageType())
        .sender(sender)
        .content(chatMessage.getContent())
        .build();

    messageRepository.save(message);
    return chatMessage;
  }

}
