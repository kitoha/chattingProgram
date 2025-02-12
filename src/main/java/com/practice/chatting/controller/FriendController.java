package com.practice.chatting.controller;

import com.practice.chatting.dto.FriendRequestDto;
import com.practice.chatting.dto.ProcessFriendRequestDto;
import com.practice.chatting.dto.ResponseDto;
import com.practice.chatting.service.friend.FriendService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @PostMapping("/request")
  public ResponseEntity<ResponseDto<String>> sendFriendRequest(@RequestBody FriendRequestDto friendRequestDto){
      ResponseDto<String> responseDto = friendService.sendFriendRequest(friendRequestDto.getFromUserId(), friendRequestDto.getToUserId());
      return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }

  @PostMapping("/request/accept")
  public ResponseEntity<ResponseDto<String>> acceptFriendRequest(@RequestBody ProcessFriendRequestDto processFriendRequestDto){
   ResponseDto<String> responseDto =  friendService.acceptFriendRequest(processFriendRequestDto.getRequestId());
   return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }

  @PostMapping("/request/reject")
  public ResponseEntity<ResponseDto<String>> rejectFriendRequest(@RequestBody ProcessFriendRequestDto processFriendRequestDto){
    ResponseDto<String> responseDto = friendService.rejectFriendRequest(processFriendRequestDto.getRequestId());
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ResponseDto<String>> deleteFriendship(@PathVariable Long friendId, Principal principal){
    ResponseDto<String> responseDto = friendService.deleteFriendShip(principal.getName(), friendId);
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }

}
