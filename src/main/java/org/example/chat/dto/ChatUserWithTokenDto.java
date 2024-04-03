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
public class ChatUserWithTokenDto {
   private Long id;
   private String username;
   private String token;
   private UserStatus status;

   public ChatUserWithTokenDto(ChatUser user, String token) {
      this.id = user.getId();
      this.username = user.getUsername();
      this.token = token;
      this.status = user.getStatus();
   }
}
