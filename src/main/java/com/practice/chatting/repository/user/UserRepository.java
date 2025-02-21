package com.practice.chatting.repository.user;

import com.practice.chatting.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  default User getByUsername(String username){
    return findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  default User getById(Long userId){
    return findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }
}
