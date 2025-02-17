package com.practice.chatting.repository;

import com.practice.chatting.domain.chat.ChatRoom;
import com.practice.chatting.domain.chat.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findByChatRoom(ChatRoom chatRoom);
  Optional<Message> findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
