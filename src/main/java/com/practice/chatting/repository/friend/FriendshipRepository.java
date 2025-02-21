package com.practice.chatting.repository.friend;

import com.practice.chatting.domain.friend.Friendship;
import com.practice.chatting.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
  Optional<Friendship> findByUserAndFriend(User user, User friend);

  List<Friendship> findAllByUser(User user);
}
