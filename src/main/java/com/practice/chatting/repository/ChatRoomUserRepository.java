package com.practice.chatting.repository;

import com.practice.chatting.domain.chat.ChatRoom;
import com.practice.chatting.domain.chat.ChatRoomUser;
import com.practice.chatting.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
  Optional<ChatRoomUser> findByChatRoomAndUserAndLeftAtIsNull(ChatRoom chatRoom, User user);
  List<ChatRoomUser> findByUserAndLeftAtIsNull(User user);
  int countByChatRoomAndLeftAtIsNull(ChatRoom chatRoom);

  void deleteAllByChatRoom(ChatRoom chatRoom);
}
