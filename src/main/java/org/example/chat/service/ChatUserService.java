package org.example.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.chat.dto.ChatUserPasswordDto;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.UserStatus;
import org.example.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final UserRepository userRepository;

    public ChatUser saveUser(ChatUserPasswordDto user) throws BadUserException {
        String username = user.getUsername();
        var temp = userRepository.findByUsername(username);
        
        if(temp.isPresent()) {
            throw new BadUserException("User: " + user.getUsername() + " already defined");

        } else {
            ChatUser newUser = user.getChatUser();
            newUser.setStatus(UserStatus.ONLINE);
            userRepository.save(newUser);
            return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        }
    }

    public Collection<ChatUser> getUsersFromConversation(Long id) {
        return this.userRepository.getUserForConversationById(id);
    }

    public Optional<ChatUser> getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
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
