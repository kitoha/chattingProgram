package com.practice.chatting.dto.friend;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestResponseDto {
  private Long requestId;
  private Long fromUserId;
  private String fromUsername;
  private LocalDateTime createdAt;
}
