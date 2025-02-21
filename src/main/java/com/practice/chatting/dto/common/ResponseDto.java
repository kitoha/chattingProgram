package com.practice.chatting.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
  private int statusCode;
  private String message;
  private T data;

  public static <T> ResponseDto<T> success(HttpStatus status, String message, T data) {
    return new ResponseDto<>(status.value(), message, data);
  }

  public static <T> ResponseDto<T> failure(HttpStatus status, String message){
    return new ResponseDto<>(status.value(), message, null);
  }

}
