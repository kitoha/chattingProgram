package com.practice.chatting.domain.friend;

import com.practice.chatting.domain.base.AuditableEntity;
import com.practice.chatting.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FriendRequest extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "from_user_id")
  private User fromUser;

  @ManyToOne
  @JoinColumn(name = "to_user_id")
  private User toUser;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  private FriendRequest(User fromUser, User toUser, RequestStatus status){
    this.fromUser = Objects.requireNonNull(fromUser, "fromUser must not be null");
    this.toUser = Objects.requireNonNull(toUser, "toUser must not be null");
    this.status = Objects.requireNonNull(status, "status must not be null");
  }

  //정적 팩토리 메서드 패턴
  public static FriendRequest create(User fromUser, User toUser){
    return new FriendRequest(fromUser, toUser, RequestStatus.PENDING);
  }

  public void accept(){
    if(this.status != RequestStatus.PENDING) {
      throw new IllegalStateException("이미 처리된 요청입니다.");
    }
    this.status = RequestStatus.ACCEPTED;
  }

  public void reject(){
    if(this.status != RequestStatus.PENDING){
      throw new IllegalStateException("이미 처리된 요청입니다.");
    }
    this.status = RequestStatus.REJECTED;
  }
}
