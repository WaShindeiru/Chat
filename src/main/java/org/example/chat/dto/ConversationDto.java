package org.example.chat.dto;

import lombok.Getter;
import org.example.chat.persistence.Conversation;

@Getter
public class ConversationDto {

   private Long id;
   private String ConversationName;

   public ConversationDto(Conversation conversation) {
      this.id = conversation.getId();
      this.ConversationName = conversation.getConversationName();
   }
}
