package com.practice.chatting.repository;

import com.practice.chatting.domain.chat.ChatRoom;
import com.practice.chatting.domain.chat.ChatRoomUser;
import com.practice.chatting.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
  List<ChatRoomUser> findByUserAndLeftAtIsNull(User user);

  int countByChatRoomAndLeftAtIsNull(ChatRoom chatRoom);
}
