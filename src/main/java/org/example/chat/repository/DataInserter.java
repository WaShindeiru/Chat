package org.example.chat.repository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chat.persistence.ChatUser;
import org.example.chat.service.ConversationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
@Slf4j
public class DataInserter implements CommandLineRunner {

    private UserRepository userRepository;
    private ConversationRepository conversationRepository;
    private MessageRepository messageCrudRepository;
    private final EntityManager manager;
    private final ConversationService service;
    private final PasswordEncoder passwordEncoder;

//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//
//        Conversation cybersecurityConversation = new Conversation("Cybersecurity");
//        Authority basicUser = new Authority("user");
//        Authority admin = new Authority("admin");
//
//        List<ChatUser> users = new ArrayList<>();
//
//        users.add(new ChatUser("Paweł Szurek", passwordEncoder.encode("123")));
//        users.add(new ChatUser("Fvlte", passwordEncoder.encode("123")));
//        users.add(new ChatUser("DefInit Luke", passwordEncoder.encode("123")));
//
//        users.forEach(cybersecurityConversation::addUser);
//        users.forEach((ChatUser user) -> user.addAuthority(basicUser));
//        users.get(0).addAuthority(admin);
//
//        List<ChatMessage> messages = new ArrayList<>();
//        messages.add(new ChatMessage("Kupcie mój kukrs", new Date(), users.get(0), cybersecurityConversation));
//        messages.add(new ChatMessage("Polska to jest taki biedny kraj", new Date(), users.get(1), cybersecurityConversation));
//        messages.add(new ChatMessage("Gatekeepują mnie :(", new Date(), users.get(2), cybersecurityConversation));
//        messages.add(new ChatMessage("Jak zainstalować Kali Linux, poradnik", new Date(), users.get(2), cybersecurityConversation));
//        messages.add(new ChatMessage("Nie ma minecrafta, usuwam server", new Date(), users.get(2), cybersecurityConversation));
//
//        messages.forEach((ChatMessage message) -> messageCrudRepository.save(message));
//
//        Conversation frontasie = new Conversation("Frontasie");
//
//        frontasie.addUser(users.get(1));
//        ChatUser Minio = new ChatUser("Minio", passwordEncoder.encode("123"));
//        ChatUser Fireship = new ChatUser("Fireship", passwordEncoder.encode("123"));
//        frontasie.addUser(Minio);
//        frontasie.addUser(Fireship);
//        Minio.addAuthority(basicUser);
//        Fireship.addAuthority(basicUser);
//
//        List<ChatMessage> jsMessage = new ArrayList<>();
//        jsMessage.add(new ChatMessage("Tylko Angualr", new Date(), Minio, frontasie));
//        jsMessage.add(new ChatMessage("I like React", new Date(), Fireship, frontasie));
//        jsMessage.add(new ChatMessage("C sigma", new Date(), users.get(1), frontasie));
//        jsMessage.add(new ChatMessage("Cword", new Date(), users.get(1), frontasie));
//
//        messageCrudRepository.saveAll(jsMessage);
//    }

    @Override
    public void run(String... args) throws Exception {

        ChatUser user = userRepository.findByUsername("Minio").orElseThrow();
        log.info(user.toString());
    }
}
