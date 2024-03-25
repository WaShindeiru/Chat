package org.example.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.chat.dto.ChatMessageDto;
import org.example.chat.dto.ChatMessageDtoWithoutId;
import org.example.chat.dto.ConversationDto;
import org.example.chat.persistence.Conversation;
import org.example.chat.security.NotAuthorizedException;
import org.example.chat.service.BadConversationException;
import org.example.chat.service.BadUserException;
import org.example.chat.service.ConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

//@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping (path="/conversation")
public class ConversationController {

    private final static Logger log = LoggerFactory.getLogger(ConversationController.class);
    private final ConversationService conversationService;

    @GetMapping(path="")
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

    @GetMapping(path="/user/{userId}")
    public ResponseEntity<?> getUserConversations(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(conversationService.getUserConversation(Long.parseLong(userId)));
        } catch (BadUserException e) {
            log.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path="/{conversationId}/message")
    public ResponseEntity<?> getConversationMessages(@PathVariable String conversationId) {
        try {
            return ResponseEntity.ok(conversationService.getConversationMessage(Long.parseLong(conversationId)).stream()
                    .map(ChatMessageDto::new).collect(Collectors.toList()));

        } catch (BadConversationException e) {
            log.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path="/{conversationId}/message")
    public ResponseEntity<?> sendConversationMessage(@PathVariable Long conversationId, @RequestBody ChatMessageDtoWithoutId message) {
        try {
            var temp = conversationService.sendMessageToConversation(conversationId, message);
            return ResponseEntity.ok().body(new ChatMessageDto(temp));
        } catch (NotAuthorizedException exception) {
            return ResponseEntity.status(403).build();
        } catch (BadConversationException | BadUserException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
