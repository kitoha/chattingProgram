package com.practice.chatting.dto;

import com.practice.chatting.domain.chat.RoomType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomDto {
  private Long id;
  private RoomType roomType;
  private String name;

  @Builder
  public ChatRoomDto(Long id, RoomType roomType, String name){
    this.id = id;
    this.roomType = roomType;
    this.name = name;
  }

}
