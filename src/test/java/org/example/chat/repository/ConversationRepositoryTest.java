package org.example.chat.repository;

import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.Conversation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ConversationRepositoryTest {

   private TestEntityManager manager;
   private ConversationRepository repository;

   @Autowired
   public ConversationRepositoryTest(ConversationRepository repository, TestEntityManager manager) {
      this.repository = repository;
      this.manager = manager;
   }

   @Test
   void getConversationsForUser() {
      // given
      ChatUser exampleUser = new ChatUser("Igor", "123");
      Conversation bicycleConversation = new Conversation("Bicycle");
      Conversation bookConversation = new Conversation("bookConversation");
      bicycleConversation.addUser(exampleUser);
      bookConversation.addUser(exampleUser);
      ChatUser persistedUser = manager.persist(exampleUser);
      manager.flush();

      // when
      Collection<Conversation> conversations = repository.getConversationsForUser(persistedUser.getId());

      // then
      assertThat(conversations.size()).isNotEqualTo(0);
      assertThat(conversations.contains(bicycleConversation)).isTrue();
      assertThat(conversations.contains(bookConversation)).isTrue();
   }
}