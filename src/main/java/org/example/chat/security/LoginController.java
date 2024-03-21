package org.example.chat.security;

import lombok.AllArgsConstructor;
import org.example.chat.dto.ChatUserDto;
import org.example.chat.dto.ChatUserPasswordDto;
import org.example.chat.persistence.ChatUser;
import org.example.chat.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
@AllArgsConstructor
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Requesting token for: " + auth.getName());
        String token = tokenService.generateToken();
        log.info("Token granted: " + token);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ChatUserPasswordDto user) {
        log.info("Attempting to register new user: " + user.getUsername());

        var temp = userRepository.findByUsername(user.getUsername());
        if(temp.isPresent()) {
            log.info("Registration unsuccessful. User: " + user.getUsername()  + " already exists");
            return ResponseEntity.badRequest().build();
        }
        else {
            userRepository.save(user.getChatUser());
            log.info("Registered new user: " + user.getUsername());
            ChatUser newUser = userRepository.findByUsername(user.getUsername()).orElseThrow(RuntimeException::new);
            return ResponseEntity.ok(new ChatUserDto(newUser));
        }
    }
}
