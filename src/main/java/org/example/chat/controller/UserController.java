package org.example.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.chat.dto.ChatUserDto;
import org.example.chat.dto.ConversationDto;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.Conversation;
import org.example.chat.service.BadUserException;
import org.example.chat.service.ChatUserService;
import org.example.chat.service.ConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

   private final ChatUserService userService;
   private final ConversationService conversationService;
   private final static Logger log = LoggerFactory.getLogger(UserController.class);

   @GetMapping(path="")
   public ResponseEntity<?> whoAmI() {
      JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
      ChatUser user = userService.getUserByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("Authenticated user doesn't exists"));
      return ResponseEntity.ok(new ChatUserDto(user));
   }

   @GetMapping(path="/conversation")
   public ResponseEntity<?> getMyConversations() {
      JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
      log.info("User " + auth.getName() + " requesting conversations");
      try {
         Collection<Conversation> conversations = conversationService.getUserConversationByName(auth.getName());
         return ResponseEntity.ok(conversations.stream()
                 .map(ConversationDto::new)
                 .collect(Collectors.toList()));

      } catch (BadUserException e) {
         log.info(e.getMessage());
         return ResponseEntity.notFound().build();
      }
   }
}
