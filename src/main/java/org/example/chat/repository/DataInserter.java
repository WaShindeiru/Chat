package org.example.chat.repository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chat.persistence.Authority;
import org.example.chat.persistence.ChatMessage;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.Conversation;
import org.example.chat.service.ConversationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
//
//        List<Conversation> conversations = new ArrayList<>();
//        conversations.add(new Conversation("Electro"));
//        conversations.add(new Conversation("Kurwy"));
//        conversations.add(new Conversation("Minio Fvlte conversation"));
//        conversations.get(0).addUser(Minio);
//        conversations.get(0).addUser(users.get(1));
//
//        conversationRepository.saveAll(conversations);
//    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Conversation bicycleConversation = new Conversation("Bicycle");
        Conversation bookConversation = new Conversation("bookConversation");
        Authority basicUser = new Authority("user");
        Authority admin = new Authority("admin");

        List<ChatUser> users = new ArrayList<>();

        users.add(new ChatUser("Igor", passwordEncoder.encode("123")));
        users.add(new ChatUser("Kacper", passwordEncoder.encode("123")));
        users.add(new ChatUser("Maciek", passwordEncoder.encode("123")));
        users.add(new ChatUser("Oliwia", passwordEncoder.encode("123")));
        users.add(new ChatUser("Ola", passwordEncoder.encode("123")));
        users.add(new ChatUser("Sebastian", passwordEncoder.encode("123")));

        users.forEach(chatUser -> chatUser.addAuthority(basicUser));
        users.get(0).addAuthority(admin);

        for (int i=0; i<users.size() / 2; i++) {
            bicycleConversation.addUser(users.get(i));
        }

        bookConversation.addUser(users.get(4));
        bookConversation.addUser(users.get(3));
        bookConversation.addUser(users.get(5));
        bookConversation.addUser(users.get(2));

        List<ChatUser> bicycleUsers = bicycleConversation.getUsers();
        List<ChatUser> bookUsers = bookConversation.getUsers();

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("My bike is broken :(", LocalDateTime.now().minusDays(1), bicycleUsers.get(0), bicycleConversation));
        messages.add(new ChatMessage("Oh no what happened?", LocalDateTime.now().minusDays(1).plusSeconds(1), bicycleUsers.get(1), bicycleConversation));
        messages.add(new ChatMessage("My front wheel is out of air", LocalDateTime.now().minusDays(1).plusSeconds(2), bicycleUsers.get(0), bicycleConversation));
        messages.add(new ChatMessage("That's terrible news, but I think I can help you with this", LocalDateTime.now().minusDays(1).plusSeconds(3), bicycleUsers.get(2), bicycleConversation));
        messages.add(new ChatMessage("Thank you so much", LocalDateTime.now().minusDays(1).plusSeconds(4), bicycleUsers.get(0), bicycleConversation));

        messages.add(new ChatMessage("What do you think about Stephen King books?", LocalDateTime.now().minusDays(1), bookUsers.get(0), bookConversation));
        messages.add(new ChatMessage("I like some of them and hate the rest", LocalDateTime.now().minusDays(1).plusSeconds(1), bookUsers.get(1), bookConversation));
        messages.add(new ChatMessage("I feel the same way", LocalDateTime.now().minusDays(1).plusSeconds(2), bookUsers.get(2), bookConversation));
        messages.add(new ChatMessage("My favourite one is Outsider", LocalDateTime.now().minusDays(1).plusSeconds(3), bookUsers.get(1), bookConversation));

        messages.forEach((ChatMessage message) -> messageCrudRepository.save(message));

        for (int i=0; i<users.size(); i++) {
            bicycleConversation.addUser(users.get(i));
        }

        conversationRepository.save(bicycleConversation);
    }

//    @Override
//    public void run(String... args) throws Exception {
//
//        ChatUser user = userRepository.findByUsername("Minio").orElseThrow();
//        log.info(user.toString());
//    }
}
