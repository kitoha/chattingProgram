package com.practice.chatting.service.friend;

import com.practice.chatting.domain.friend.FriendRequest;
import com.practice.chatting.domain.friend.Friendship;
import com.practice.chatting.domain.friend.RequestStatus;
import com.practice.chatting.domain.user.User;
import com.practice.chatting.dto.ResponseDto;
import com.practice.chatting.repository.FriendRequestRepository;
import com.practice.chatting.repository.FriendshipRepository;
import com.practice.chatting.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

  private final FriendRequestRepository friendRequestRepository;
  private final FriendshipRepository friendshipRepository;
  private final UserRepository userRepository;

  @Transactional
  public ResponseDto<String> sendFriendRequest(Long fromUserId, Long toUserId){
    try {
      User fromUser = userRepository.findById(fromUserId)
          .orElseThrow(() -> new RuntimeException("User not found"));
      User toUser = userRepository.findById(toUserId)
          .orElseThrow(() -> new RuntimeException("Target user not found"));

      validateNoDuplicateFriendRequest(fromUser, toUser);

      FriendRequest request = FriendRequest.create(fromUser, toUser);
      friendRequestRepository.save(request);
      return ResponseDto.failure(HttpStatus.OK, "친구 요청이 성공하였습니다.");
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "친구 요청이 실패하였습니다.");
    }
  }

  @Transactional
  public ResponseDto<String> acceptFriendRequest(Long friendRequestId){
    try {
      FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
          .orElseThrow(() -> new RuntimeException("친구 요청을 찾을 수 없습니다. id: " + friendRequestId));

      if (RequestStatus.PENDING.equals(friendRequest.getStatus())) {
        throw new RuntimeException("이미 처리된 친구 요청입니다.");
      }

      friendRequest.accept();
      createFriendShip(friendRequest.getToUser(), friendRequest.getFromUser());
      createFriendShip(friendRequest.getFromUser(), friendRequest.getToUser());
      return ResponseDto.failure(HttpStatus.OK, "친구 요청이 성공하였습니다.");
    }catch (Exception exception){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "친구 수락 요청이 실패하였습니다.");
    }
  }

  @Transactional
  public ResponseDto<String> rejectFriendRequest(Long friendRequestId){
    try {
      FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
          .orElseThrow(() -> new RuntimeException("친구 요청을 찾을 수 없습니다. id: " + friendRequestId));

      friendRequest.reject();
      return ResponseDto.failure(HttpStatus.OK, "친구 거절 요청이 성공하였습니다.");
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "친구 거절 요청이 실패하였습니다.");
    }
  }

  @Transactional
  public ResponseDto<String> deleteFriendShip(String userName, Long friendId){
    try {
      User user = userRepository.findByUsername(userName)
          .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userName: " + userName));
      User friend = userRepository.findById(friendId)
          .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. id: " + friendId));

      friendshipRepository.findByUserAndFriend(user, friend)
          .ifPresent(friendshipRepository::delete);
      friendshipRepository.findByUserAndFriend(friend, user)
          .ifPresent(friendshipRepository::delete);
      return ResponseDto.failure(HttpStatus.OK, "친구 삭제 요청이 성공하였습니다.");
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "친구 삭제 요청이 실패하였습니다.");
    }
  }

  private void createFriendShip(User user, User friend){
    Friendship friendship = new Friendship(user, friend);
    friendshipRepository.save(friendship);
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
