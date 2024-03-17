package org.example.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.chat.persistence.ChatUser;
import org.example.chat.persistence.UserStatus;
import org.example.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final UserRepository userRepository;

    public void saveUser(ChatUser user) throws BadUserException {
        var temp = userRepository.findByUsername(user.getUsername());
        if(temp.isPresent()) {
            throw new BadUserException("User: " + user.getUsername() + " already defined");
        } else {
            user.setStatus(UserStatus.ONLINE);
            userRepository.save(user);
        }
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
