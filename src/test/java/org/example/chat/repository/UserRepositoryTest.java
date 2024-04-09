package org.example.chat.repository;

import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

   private TestEntityManager manager;
   private UserRepository repository;

   @Autowired
   public UserRepositoryTest(TestEntityManager manager, UserRepository repository) {
      this.manager = manager;
      this.repository = repository;
   }

   @Test
   void findByUsername() {
      // given
      String username = "Maciek";
      ChatUser user = new ChatUser(username, "stronkPassword", UserStatus.ONLINE);
      manager.persist(user);
      manager.flush();

      // when
      Optional<ChatUser> resultUser = this.repository.findByUsername(username);

      // then
      assertThat(resultUser.isPresent()).isTrue();
      assertThat(resultUser.get().getUsername()).isEqualTo(username);

      manager.remove(user);
   }

   @Test
   void getUserForConversationById() {
      // given
      String username = "Ola";

      ChatUser user2 = new ChatUser(username, "strongPassword", UserStatus.ONLINE);
      ChatUser managerUser =  manager.persist(user2);
      manager.flush();

      // when
      Optional<ChatUser> resultUser = this.repository.findById(managerUser.getId());

      // then
      assertThat(resultUser.isPresent()).isTrue();
      assertThat(resultUser.get().getUsername()).isEqualTo(username);
      assertThat(resultUser.get().getId()).isEqualTo(managerUser.getId());
   }
}