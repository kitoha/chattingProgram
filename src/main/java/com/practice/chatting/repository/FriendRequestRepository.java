package com.practice.chatting.repository;

import com.practice.chatting.domain.friend.FriendRequest;
import com.practice.chatting.domain.friend.RequestStatus;
import com.practice.chatting.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
  List<FriendRequest> findByToUserAndStatus(User toUser, RequestStatus status);

}
