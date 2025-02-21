package com.practice.chatting.repository;

import com.practice.chatting.domain.chat.ChatRoom;
import com.practice.chatting.domain.chat.RoomType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  @Query("select cr from ChatRoom cr " +
      "join ChatRoomUser cru1 on cr.id = cru1.chatRoom.id " +
      "join ChatRoomUser cru2 on cr.id = cru2.chatRoom.id " +
      "where cr.roomType = :roomType " +
      "and cru1.user.id = :userId1 " +
      "and cru2.user.id = :userId2 " +
      "and cru1.leftAt is null and cru2.leftAt is null")
  Optional<ChatRoom> findOneToOneChatRoom(@Param("userId1") Long userId1,
      @Param("userId2") Long userId2,
      @Param("roomType") RoomType roomType);

  default ChatRoom getById(Long chatRoomId){
    return findById(chatRoomId)
        .orElseThrow(() -> new RuntimeException("ChatRoom not found: " + chatRoomId));
  }
}
