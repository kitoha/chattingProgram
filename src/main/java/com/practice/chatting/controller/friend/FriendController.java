package com.practice.chatting.controller.friend;

import com.practice.chatting.dto.friend.FriendDto;
import com.practice.chatting.dto.friend.FriendRequestResponseDto;
import com.practice.chatting.dto.friend.ProcessFriendRequestDto;
import com.practice.chatting.dto.common.ResponseDto;
import com.practice.chatting.service.friend.FriendService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {

  private final FriendService friendService;

  /*친구 요청 보내기*/
  @PostMapping("/request/{friendId}")
  public ResponseEntity<ResponseDto<String>> sendFriendRequest(@PathVariable Long friendId, Principal principal){
      ResponseDto<String> responseDto = friendService.sendFriendRequest(principal.getName(), friendId);
      return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }
  /*친구 요청 수락*/
  @PostMapping("/request/accept")
  public ResponseEntity<ResponseDto<String>> acceptFriendRequest(@RequestBody ProcessFriendRequestDto processFriendRequestDto){
   ResponseDto<String> responseDto =  friendService.acceptFriendRequest(processFriendRequestDto.getRequestId());
   return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }
  /*친구 요청 거절*/
  @PostMapping("/request/reject")
  public ResponseEntity<ResponseDto<String>> rejectFriendRequest(@RequestBody ProcessFriendRequestDto processFriendRequestDto){
    ResponseDto<String> responseDto = friendService.rejectFriendRequest(processFriendRequestDto.getRequestId());
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }
  /*친구 삭제*/
  @DeleteMapping("/delete/friendId")
  public ResponseEntity<ResponseDto<String>> deleteFriendship(@PathVariable Long friendId, Principal principal){
    ResponseDto<String> responseDto = friendService.deleteFriendShip(principal.getName(), friendId);
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }
  /*친구 목록 조회*/
  @GetMapping("")
  public ResponseEntity<ResponseDto<List<FriendDto>>> getFriendList(Principal principal){
    ResponseDto<List<FriendDto>> responseDto = friendService.getFriendList(principal.getName());
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }
  /*친구 요청 목록 조회*/
  @GetMapping("/requests")
  public ResponseEntity<ResponseDto<List<FriendRequestResponseDto>>> getReceivedFriendRequests(Principal principal){
    ResponseDto<List<FriendRequestResponseDto>> responseDto = friendService.getFriendRequestList(principal.getName());
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }

}
