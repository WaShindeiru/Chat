package org.example.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.chat.dto.ConversationDto;
import org.example.chat.persistence.Conversation;
import org.example.chat.service.BadConversationException;
import org.example.chat.service.BadUserException;
import org.example.chat.service.ConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            return ResponseEntity.ok(conversationService.getConversationMessage(Long.parseLong(conversationId)));
        } catch (BadConversationException e) {
            log.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
