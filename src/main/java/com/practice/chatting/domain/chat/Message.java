package com.practice.chatting.domain.chat;

import com.practice.chatting.domain.base.AuditableEntity;
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
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Message extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id", nullable = false)
  private ChatRoom chatRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  private User sender;

  @Column(nullable = false)
  private String content;

  private Message(User sender,ChatRoom chatRoom, String content){
    this.sender = Objects.requireNonNull(sender, "sender must not be null");
    this.chatRoom = Objects.requireNonNull(chatRoom, "chatRoom must not be null");
    this.content = Objects.requireNonNull(content, "content must not be null");
  }

  public static Message create(User sender,ChatRoom chatRoom, String content){
    return new Message(sender,chatRoom, content);
  }

}
