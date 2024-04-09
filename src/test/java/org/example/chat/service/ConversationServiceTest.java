package org.example.chat.service;

import org.example.chat.dto.ChatUserDto;
import org.example.chat.dto.ConversationDtoWithoutId;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.Conversation;
import org.example.chat.persistence.UserStatus;
import org.example.chat.repository.ConversationRepository;
import org.example.chat.repository.MessageRepository;
import org.example.chat.repository.UserRepository;
import org.example.chat.websocket.WebSocketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

   @Mock
   private ConversationRepository conversationRepository;
   @Mock
   private UserRepository userRepository;
   @Mock
   private MessageRepository messageRepository;
   @Mock
   private WebSocketService webSocketService;
   @InjectMocks
   private ConversationService conversationService;

   @Test
   void createConversation() throws BadConversationException {
      String username = "Arek";
      String conversationName = "book";
      Long conversationId = 6L;
      ChatUser newUser = new ChatUser(username, "123", UserStatus.ONLINE);
      ConversationDtoWithoutId newConversation = new ConversationDtoWithoutId(conversationName);

      Mockito.when(userRepository.findByUsername(username))
              .thenReturn(Optional.of(new ChatUser(5L, username, "123", UserStatus.ONLINE)));

      Mockito.when(conversationRepository.findConversationByName("book")).thenReturn(Optional.empty());

      Conversation mockConversation = new Conversation();
      mockConversation.setConversationName(conversationName);
      mockConversation.setId(conversationId);
      mockConversation.addUser(newUser);

      Mockito.when(conversationRepository.save(Mockito.any(Conversation.class))).thenReturn(mockConversation);

      this.conversationService.createConversation(newConversation, username);

      ArgumentCaptor<Conversation> conversationCaptor = ArgumentCaptor.forClass(Conversation.class);
      Mockito.verify(conversationRepository).save(conversationCaptor.capture());
      Conversation capturedConversation = conversationCaptor.getValue();

      assertThat(capturedConversation.getConversationName()).isEqualTo(conversationName);
      assertThat(capturedConversation.getUsers().contains(newUser)).isTrue();
   }

   @Test
   void createConversationWhenConversationExists() throws BadConversationException {
      String username = "Arek";
      String conversationName = "book";
      Long conversationId = 6L;
      ChatUser newUser = new ChatUser(username, "123", UserStatus.ONLINE);
      ConversationDtoWithoutId newConversation = new ConversationDtoWithoutId(conversationName);
      Conversation persistedConversation = new Conversation(conversationId, conversationName);

      Mockito.when(userRepository.findByUsername(username))
              .thenReturn(Optional.of(new ChatUser(5L, username, "123", UserStatus.ONLINE)));

      Mockito.when(conversationRepository.findConversationByName(conversationName)).thenReturn(
              Optional.of(persistedConversation)
      );

      assertThatThrownBy(() -> this.conversationService.createConversation(newConversation, username))
              .isInstanceOf(BadConversationException.class);
   }

   @Test
   void addUserToConversation() throws BadConversationException, BadUserException {
      Long userId = 3L;
      Long conversationId = 10L;
      ChatUser user = new ChatUser(userId, "Maciek", "123", UserStatus.ONLINE);
      ChatUserDto userDto = new ChatUserDto(user);
      Conversation conversationMock = Mockito.mock(Conversation.class);

      Mockito.when(this.userRepository.findById(userId)).thenReturn(
              Optional.of(user)
      );

      Mockito.when(this.conversationRepository.findConversationById(conversationId)).thenReturn(
              Optional.of(conversationMock)
      );

      this.conversationService.addUserToConversation(userDto, conversationId);

      Mockito.verify(conversationMock).addUser(user);
   }

   @Test
   void addUserToConversationWhenConversationDoesNotExist() {
      Long userId = 3L;
      Long conversationId = 10L;
      ChatUser user = new ChatUser(userId, "Maciek", "123", UserStatus.ONLINE);
      ChatUserDto userDto = new ChatUserDto(user);

      Mockito.when(this.userRepository.findById(userId)).thenReturn(
              Optional.of(user)
      );

      Mockito.when(this.conversationRepository.findConversationById(conversationId)).thenReturn(
              Optional.empty()
      );

      assertThatThrownBy(() -> this.conversationService.addUserToConversation(userDto, conversationId))
              .isInstanceOf(BadConversationException.class);
   }
}