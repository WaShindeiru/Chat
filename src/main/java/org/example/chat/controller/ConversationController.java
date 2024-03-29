package org.example.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.chat.dto.*;
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

    @GetMapping(path="")
    public ResponseEntity<?> getAllConversations() {
        return ResponseEntity.ok(this.conversationService.getAllConversations());
    }

    @PostMapping(path="/{conversationId}/user")
    public ResponseEntity<?> addUserToConversation(@RequestBody ChatUserDto user, @PathVariable Long conversationId) {
        try {
            var auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            var temp = this.conversationService.addUserToConversation(user, conversationId);

            Collection<Conversation> conversations = conversationService.getUserConversationByName(auth.getName());
            return ResponseEntity.ok(conversations.stream()
                    .map(ConversationDto::new)
                    .collect(Collectors.toList()));

        } catch(BadUserException | BadConversationException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping(path="")
    public ResponseEntity<?> createConversation(@RequestBody ConversationDtoWithoutId conversation) {
        var auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        try {
            ConversationDto persistedConversation = this.conversationService.createConversation(conversation, userName);
            return ResponseEntity.ok(persistedConversation);
        } catch (BadConversationException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
