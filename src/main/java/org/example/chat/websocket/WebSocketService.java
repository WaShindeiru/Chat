package org.example.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.chat.dto.ChatMessageDto;
import org.example.chat.dto.ConversationDto;
import org.example.chat.persistence.ChatUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebSocketService {

   private final Map<String, WebSocketSession> sessions;
   private final ObjectMapper mapper;
   private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);

   public void sendMessageToAllActiveUsers(ChatMessageDto message, Collection<ChatUser> users) {
      List<WebSocketSession> activeSessions = new ArrayList<>();

      for(ChatUser user : users) {
         if(sessions.containsKey(user.getUsername())) {
            activeSessions.add(sessions.get(user.getUsername()));
         }
      }

      WebSocketMessage chatMessage = new WebSocketMessage();
      chatMessage.setMessage(message);

      activeSessions.forEach((var session) -> {
         try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessage)));
         } catch (IOException e) {
            log.info("Mapping unsuccessful");
         }
      });
   }

   public void sendNewConversationNotification(ConversationDto conversation) {

      WebSocketMessage chatMessage = new WebSocketMessage();
      chatMessage.setConversation(conversation);

      for(Map.Entry<String, WebSocketSession> session : this.sessions.entrySet()) {
         try {
            session.getValue().sendMessage(new TextMessage(mapper.writeValueAsString(chatMessage)));
         } catch (IOException e) {
            log.info("Mapping unsuccessful");
         }
      }
   }
}
