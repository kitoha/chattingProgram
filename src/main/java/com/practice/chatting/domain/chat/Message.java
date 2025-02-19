package com.practice.chatting.domain.chat;

import com.practice.chatting.domain.base.AuditableEntity;
import com.practice.chatting.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
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

  @Enumerated(EnumType.STRING)
  @Column(name = "message_type", nullable = false)
  private MessageType messageType;

  @Builder
  private Message(User sender,ChatRoom chatRoom, String content, MessageType messageType){
    this.sender = sender;
    this.chatRoom = chatRoom;
    this.content = content;
    this.messageType = messageType;
  }

}
