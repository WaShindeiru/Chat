package org.example.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.chat.persistence.ChatMessage;

import java.util.Date;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class ChatMessageDto {

   private Long id;
   private String messageText;
   private Date sentDateTime;
   private ChatUserDto sentBy;
   private ConversationDto conversation;

   public ChatMessageDto(ChatMessage message) {
      this.id = message.getId();
      this.messageText = message.getMessageText();
      this.sentDateTime = message.getSentDateTime();
      this.sentBy = new ChatUserDto(message.getSentBy());
      this.conversation = new ConversationDto(message.getOrigin());
   }
}
