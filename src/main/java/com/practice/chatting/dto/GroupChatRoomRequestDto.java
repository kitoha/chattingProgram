package com.practice.chatting.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class GroupChatRoomRequestDto {
  private List<Long> participantIds;

  private String groupName;
}
