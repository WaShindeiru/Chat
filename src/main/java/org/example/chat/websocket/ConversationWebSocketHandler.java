package org.example.chat.websocket;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;

@RequiredArgsConstructor
public class ConversationWebSocketHandler extends AbstractWebSocketHandler {

   private final Map<String, WebSocketSession> sessions;
   private static final Logger log = LoggerFactory.getLogger(ConversationWebSocketHandler.class);

   @Override
   public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      var principal = session.getPrincipal();

      if (principal == null || principal.getName() == null) {
         log.info("User is not authenticated");
         session.close(CloseStatus.SERVER_ERROR.withReason("User must be authenticated"));
      }

      sessions.put(principal.getName(), session);
   }

   @Override
   public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
      var principal = session.getPrincipal();
      sessions.remove(principal.getName());
   }
}
