package org.example.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WebSocketController {

   private final Map<String, WebSocketSession> sessions;
   private final ObjectMapper mapper;
   private final static Logger log = LoggerFactory.getLogger(WebSocketController.class);

   @PostMapping(value = "/test")
   public ResponseEntity<?> sendMessage(@RequestBody Car message) throws IOException {

      var auth = SecurityContextHolder.getContext().getAuthentication();
      String userName = auth.getName();
      log.info("Sending message back to: " + userName);

      var session = sessions.get(userName);
      session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));

      return ResponseEntity.ok(userName);
   }

}
