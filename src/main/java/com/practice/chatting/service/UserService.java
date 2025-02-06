package com.practice.chatting.service;

import com.practice.chatting.domain.user.User;
import com.practice.chatting.dto.ResponseDto;
import com.practice.chatting.dto.UserRequestDto;
import com.practice.chatting.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  public ResponseDto<String> register(UserRequestDto userDto){
    if(userRepository.findByUsername(userDto.getUsername()).isPresent()){
      return ResponseDto.failure(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다.");
    }

    User user = new User(userDto.getUsername(), userDto.getEmail(),passwordEncoder.encode(userDto.getPassword()));
    userRepository.save(user);
    return ResponseDto.success(HttpStatus.CREATED, "회원가입 성공!", null);
  }

  public ResponseDto<String> login(UserRequestDto loginDto, HttpServletRequest request){
    try {

      UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());

      if (!passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
        return ResponseDto.failure(HttpStatus.UNAUTHORIZED, "Invalid credentials");
      }

      Authentication authentication = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
      );

      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(authentication);
      SecurityContextHolder.setContext(securityContext);

      HttpSession session = request.getSession(true);
      session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

      return ResponseDto.success(HttpStatus.OK, "Logged in successfully",null);
    } catch (Exception ex) {
      return ResponseDto.failure(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
  }

}
