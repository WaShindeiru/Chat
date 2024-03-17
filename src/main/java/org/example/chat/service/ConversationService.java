package org.example.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.chat.persistence.ChatMessage;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.Conversation;
import org.example.chat.repository.ConversationRepository;
import org.example.chat.repository.MessageRepository;
import org.example.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

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

    public Iterable<ChatMessage> getConversationMessage(Long conversationId) throws BadConversationException {
        Optional<Conversation> temp = conversationRepository.findById(conversationId);
        temp.orElseThrow(() -> new BadConversationException("Conversation with id: " + conversationId + " doesn't exists"));
        return messageRepository.getMessagesFromConversation(conversationId);
    }
}
