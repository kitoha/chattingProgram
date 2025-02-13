package com.practice.chatting.repository;

import com.practice.chatting.domain.chat.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
