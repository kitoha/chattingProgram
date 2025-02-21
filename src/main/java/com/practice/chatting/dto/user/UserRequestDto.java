package com.practice.chatting.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
  private String username;
  private String email;
  private String password;
}
