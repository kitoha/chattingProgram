package com.practice.chatting.domain.chat;

import com.practice.chatting.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private User sender;

  private String receiver;

  @Column(nullable = false)
  private String content;

  private LocalDateTime sendAt;

  private Message(User sender, String receiver, String content, LocalDateTime sendAt){
    this.sender = Objects.requireNonNull(sender, "sender must not be null");
    this.receiver = Objects.requireNonNull(receiver, "receiver must not be null");
    this.content = Objects.requireNonNull(content, "content must not be null");
    this.sendAt = Objects.requireNonNull(sendAt, "sendAt must not be null");
  }

  public static Message create(User sender, String receiver, String content, LocalDateTime sendAt){
    return new Message(sender, receiver, content, sendAt);
  }

}
