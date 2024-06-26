package org.example.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.UserStatus;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class ChatUserDto {
   private Long id;
   private String username;
   private UserStatus status;

   public ChatUserDto(ChatUser user) {
      this.id = user.getId();
      this.username = user.getUsername();
      this.status = user.getStatus();
   }
}
