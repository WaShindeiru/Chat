package org.example.chat.websocketold;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;


@AllArgsConstructor
public class MessageController {

   private final static Logger log = LoggerFactory.getLogger(MessageController.class);
   private final SimpMessagingTemplate template;

   @MessageMapping("/news")
   public void sendNews(@Payload SimpleMessage message) {
      var auth = SecurityContextHolder.getContext().getAuthentication();
      log.info("Message sent by: " + auth.getPrincipal());
      log.info("Message received: " + message);
      this.template.convertAndSend("/queue/news", message);
   }
}
