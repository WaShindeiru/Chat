package org.example.chat.dto;

import lombok.Getter;
import org.example.chat.persistence.ChatMessage;

import java.util.Date;

@Getter
public class ChatMessageDto {

   private Long id;
   private String messageText;
   private Date sentDateTime;
   private ChatUserDto sentBy;

   public ChatMessageDto(ChatMessage message) {
      this.id = message.getId();
      this.messageText = message.getMessageText();
      this.sentDateTime = message.getSentDateTime();
      this.sentBy = new ChatUserDto(message.getSentBy());
   }
}
