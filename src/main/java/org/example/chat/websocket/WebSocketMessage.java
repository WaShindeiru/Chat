package org.example.chat.websocket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.chat.dto.ChatMessageDto;
import org.example.chat.dto.ConversationDto;

@Getter
@Setter
@NoArgsConstructor
public class WebSocketMessage {

   private ChatMessageDto message;
   private ConversationDto conversation;
}
