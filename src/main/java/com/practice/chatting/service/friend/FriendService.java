package com.practice.chatting.service.friend;

import com.practice.chatting.domain.friend.FriendRequest;
import com.practice.chatting.domain.friend.Friendship;
import com.practice.chatting.domain.friend.RequestStatus;
import com.practice.chatting.domain.user.User;
import com.practice.chatting.dto.FriendDto;
import com.practice.chatting.dto.FriendRequestResponseDto;
import com.practice.chatting.dto.ResponseDto;
import com.practice.chatting.repository.FriendRequestRepository;
import com.practice.chatting.repository.FriendshipRepository;
import com.practice.chatting.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import java.util.List;
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
  public ResponseDto<String> sendFriendRequest(String fromUsername, Long toUserId){
    try {
      User fromUser = userRepository.getByUsername(fromUsername);
      User toUser = userRepository.getById(toUserId);

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
      FriendRequest friendRequest = friendRequestRepository.getById(friendRequestId);

      if (!RequestStatus.PENDING.equals(friendRequest.getStatus())) {
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
      User user = userRepository.getByUsername(userName);
      User friend = userRepository.getById(friendId);

      friendshipRepository.findByUserAndFriend(user, friend)
          .ifPresent(friendshipRepository::delete);
      friendshipRepository.findByUserAndFriend(friend, user)
          .ifPresent(friendshipRepository::delete);
      return ResponseDto.failure(HttpStatus.OK, "친구 삭제 요청이 성공하였습니다.");
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "친구 삭제 요청이 실패하였습니다.");
    }
  }

  @Transactional(readOnly = true)
  public ResponseDto<List<FriendDto>> getFriendList(String username){
    try {
      User user = userRepository.getByUsername(username);
      List<Friendship> friendships = friendshipRepository.findAllByUser(user);
      List<FriendDto> friendDtoList = friendships.stream()
          .map(friendship -> new FriendDto(friendship.getFriend().getId(),
              friendship.getFriend().getUsername()))
          .toList();
      return ResponseDto.success(HttpStatus.OK,"목록 조회가 성공하였습니다." ,friendDtoList);
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "목록 조회가 실패하였습니다.");
    }
  }

  @Transactional(readOnly = true)
  public ResponseDto<List<FriendRequestResponseDto>> getFriendRequestList(String username){
    try {
      User user = userRepository.getByUsername(username);
      List<FriendRequest> requests = friendRequestRepository.findByToUserAndStatus(user,
          RequestStatus.PENDING);
      List<FriendRequestResponseDto> friendRequestResponseDtoList = requests.stream()
          .map(request -> new FriendRequestResponseDto(request.getId(),
              request.getFromUser().getId(),
              request.getFromUser().getUsername(),
              request.getCreatedAt()))
          .toList();
      return ResponseDto.success(HttpStatus.OK, "친구 요청 목록 조회 성공", friendRequestResponseDtoList);
    }catch (Exception e){
      return ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "친구 요청 목록 조회가 실패하였습니다.");
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
