package com.practice.chatting.formater;

import static com.practice.chatting.common.constants.COMMA;
import static com.practice.chatting.common.constants.ELLIPSIS;

import com.practice.chatting.domain.user.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;

public class GroupNameFormatter {
  private static final int MAX_LENGTH = 15;

  public static String formatGroupName(List<User> participants,String groupName) {
    if (!ObjectUtils.isEmpty(groupName)) {
      return groupName;
    }

    List<String> names = participants.stream()
        .map(User::getUsername)
        .collect(Collectors.toList());
    String joinedNames = String.join(COMMA, names);
    return truncate(joinedNames);
  }

  private static String truncate(String input) {
    if (input.length() <= MAX_LENGTH) {
      return input;
    }
    return input.substring(0, MAX_LENGTH) + ELLIPSIS;
  }
}
