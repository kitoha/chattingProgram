package com.practice.chatting.service;

import com.practice.chatting.domain.friend.FriendRequest;
import com.practice.chatting.domain.friend.RequestStatus;
import com.practice.chatting.domain.user.User;
import com.practice.chatting.repository.FriendRequestRepository;
import com.practice.chatting.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

  private final FriendRequestRepository friendRequestRepository;
  private final UserRepository userRepository;

  @Transactional
  public void sendFriendRequest(String fromUsername, String toUsername){
    User fromUser = userRepository.findByUsername(fromUsername)
        .orElseThrow(()-> new RuntimeException("User not found"));
    User toUser = userRepository.findByUsername(toUsername)
        .orElseThrow(() -> new RuntimeException("Target user not found"));

    validateNoDuplicateFriendRequest(fromUser, toUser);

    FriendRequest request = FriendRequest.create(fromUser,toUser);
    friendRequestRepository.save(request);
  }

  private void validateNoDuplicateFriendRequest(User fromUser, User toUser){
    Optional<FriendRequest> existingRequest = friendRequestRepository.findByFromUserAndToUserWithLock(fromUser, toUser);
    if(existingRequest.isPresent()){
      RequestStatus status = existingRequest.get().getStatus();
      if(status == RequestStatus.PENDING || status == RequestStatus.ACCEPTED){
        throw new DuplicateRequestException("이미 친구 요청이 있거나 친구 관계입니다.");
      }
    }
  }

}
