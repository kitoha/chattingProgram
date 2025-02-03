package com.practice.chatting.repository;

import com.practice.chatting.domain.friend.FriendRequest;
import com.practice.chatting.domain.friend.RequestStatus;
import com.practice.chatting.domain.user.User;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
  List<FriendRequest> findByToUserAndStatus(User toUser, RequestStatus status);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<FriendRequest> findByFromUserAndToUserWithLock(User fromUser, User toUser);

}
