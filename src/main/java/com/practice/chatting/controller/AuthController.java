package com.practice.chatting.controller;

import com.practice.chatting.dto.ResponseDto;
import com.practice.chatting.dto.UserRequestDto;
import com.practice.chatting.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<ResponseDto<String>> register(@RequestBody UserRequestDto userDto){
    ResponseDto<String> responseDto = userService.register(userDto);
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseDto<String>> login(@RequestBody UserRequestDto userDto,HttpServletRequest request){
    ResponseDto<String> responseDto = userService.login(userDto,request);
    return ResponseEntity.status(responseDto.getStatusCode()).body(responseDto);
  }
}
