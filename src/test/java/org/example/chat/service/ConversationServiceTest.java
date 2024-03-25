package org.example.chat.service;

import org.example.chat.persistence.ChatMessage;
import org.example.chat.persistence.Conversation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConversationServiceTest {

   @Test
   void getConversationMessage(ConversationService service) throws BadConversationException {

      Iterable<ChatMessage> temp = service.getConversationMessage(1L);
   }
}