package com.practice.chatting.domain.chat;

import com.practice.chatting.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoomUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id", nullable = false)
  private ChatRoom chatRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id",nullable = false)
  private User user;

  @Column(name = "joined_at", nullable = false)
  private LocalDateTime joinedAt;

  @Column(name = "left_at")
  private LocalDateTime leftAt;

  @Builder
  public ChatRoomUser(ChatRoom chatRoom, User user, LocalDateTime joinedAt, LocalDateTime leftAt){
    this.chatRoom = chatRoom;
    this.user = user;
    this.joinedAt = joinedAt;
    this.leftAt = leftAt;
  }

}
