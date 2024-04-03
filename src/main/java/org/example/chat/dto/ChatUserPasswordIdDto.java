package org.example.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.chat.persistence.ChatUser;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatUserPasswordIdDto {
   private Long id;
   private String username;
   private String password;

   public ChatUserPasswordIdDto(ChatUser user) {
      this.id = user.getId();
      this.username = user.getUsername();
      this.password = user.getPassword();
   }
}
