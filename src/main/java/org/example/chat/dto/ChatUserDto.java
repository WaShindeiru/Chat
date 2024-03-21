package org.example.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.UserStatus;

@Getter
@NoArgsConstructor
@Setter
public class ChatUserDto {
   private Long userId;
   private String username;
   private UserStatus status;

   public ChatUserDto(ChatUser user) {
      this.userId = user.getId();
      this.username = user.getUsername();
      this.status = user.getStatus();
   }
}
