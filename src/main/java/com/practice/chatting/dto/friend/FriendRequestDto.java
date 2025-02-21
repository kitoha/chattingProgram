package com.practice.chatting.dto.friend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestDto {
  private Long fromUserId;
  private Long toUserId;
}
