package org.example.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.chat.dto.ChatMessageDto;
import org.example.chat.dto.ChatMessageDtoWithoutId;
import org.example.chat.persistence.ChatMessage;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.Conversation;
import org.example.chat.repository.ConversationRepository;
import org.example.chat.repository.MessageRepository;
import org.example.chat.repository.UserRepository;
import org.example.chat.security.NotAuthorizedException;
import org.example.chat.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final static Logger log = LoggerFactory.getLogger(ConversationService.class);
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final Map<String, WebSocketSession> sessions;
    private final WebSocketService webSocketService;

    public Collection<Conversation> getUserConversation(Long userId) throws BadUserException {
        Optional<ChatUser> storedUser = userRepository.findById(userId);
        storedUser.orElseThrow(() -> new BadUserException("User with id: " + userId + " doesn't exists"));
        return conversationRepository.getConversationsForUser(userId);
    }

    public Collection<Conversation> getUserConversationByName(String username) throws BadUserException {
        Optional<ChatUser> temp = userRepository.findByUsername(username);
        ChatUser storedUser = temp.orElseThrow(() -> new BadUserException("User with name: " + username + " doesn't exists"));
        return conversationRepository.getConversationsForUser(storedUser.getId());
    }

    public Collection<ChatMessage> getConversationMessage(Long conversationId) throws BadConversationException {
        Optional<Conversation> temp = conversationRepository.findById(conversationId);
        temp.orElseThrow(() -> new BadConversationException("Conversation with id: " + conversationId + " doesn't exists"));
        return messageRepository.getMessagesFromConversation(conversationId);
    }

    @Transactional
    public ChatMessage sendMessageToConversation(Long conversationId, ChatMessageDtoWithoutId message) throws NotAuthorizedException, BadConversationException, BadUserException {
        log.info("Attemtping to persist message: " + message);
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        ChatUser user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new RuntimeException("Authenticated user doesn't exists"));
        ChatUser sender = userRepository.findById(message.getSentBy().getUserId()).orElseThrow(
                () -> new BadUserException("User with id: " + message.getSentBy().getUserId() + "doesn't exists"));
        Conversation conversation = conversationRepository.findConversationById(conversationId).orElseThrow(
                () -> new BadConversationException("Conversation with id: " + conversationId + "doesn't exists"));
        Collection<ChatUser> users = userRepository.getUserForConversationById(conversationId);

        if(!users.contains(user) || user != sender) {
            throw new NotAuthorizedException();
        }

        ChatMessage messageDomain = new ChatMessage(message);
        messageDomain.setSentBy(sender);
        messageDomain.setMessageConversation(conversation);

        ChatMessage persistedMessage = this.messageRepository.save(messageDomain);

        webSocketService.sendMessageToAllActiveUsers(new ChatMessageDto(persistedMessage), users);

        return persistedMessage;
    }
}
