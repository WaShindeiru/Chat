package org.example.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.chat.dto.ChatUserPasswordDto;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.UserStatus;
import org.example.chat.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChatUser saveUser(ChatUserPasswordDto user) throws BadUserException {
        String username = user.getUsername();
        var temp = userRepository.findByUsername(username);
        
        if(temp.isPresent()) {
            throw new BadUserException("User: " + user.getUsername() + " already defined");

        } else {
            ChatUser newUser = user.getChatUser();
            newUser.setStatus(UserStatus.ONLINE);
            newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
            return userRepository.save(newUser);
        }
    }

    public Collection<ChatUser> getUsersFromConversation(Long id) {
        return this.userRepository.getUserForConversationById(id);
    }

    public ChatUser getUserByUsername(String username) throws BadUserException {
        return this.userRepository.findByUsername(username).orElseThrow(
                () -> new BadUserException("Authenticated user with username: " + username + "doesn't exist"));
    }

    public void disconnect(ChatUser user) {
        var storedUser = userRepository.findById(user.getId());
        if (storedUser.isPresent()) {
            storedUser.get().setStatus(UserStatus.ONLINE);
        } else {
            throw new RuntimeException("User with id: " + user.getId() + " doesn't exist" );
        }
    }


}
