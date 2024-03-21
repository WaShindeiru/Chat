package org.example.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.UserStatus;

@Getter
@Setter
@NoArgsConstructor
public class ChatUserPasswordDto {
   private String username;
   private String password;

   public ChatUserPasswordDto(ChatUser user) {
      this.username = user.getUsername();
      this.password = user.getPassword();
   }

   public ChatUser getChatUser() {
      return new ChatUser(this.username, this.password);
   }
}

