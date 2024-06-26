package org.example.chat.security;

import lombok.AllArgsConstructor;
import org.example.chat.dto.ChatUserPasswordDto;
import org.example.chat.dto.ChatUserWithTokenDto;
import org.example.chat.persistence.ChatUser;
import org.example.chat.service.BadUserException;
import org.example.chat.service.ChatUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
@AllArgsConstructor
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final TokenService tokenService;
    private final ChatUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Requesting token for: " + auth.getName());

        try {
            var tempUser = this.userService.getUserByUsername(auth.getName());
            String token = tokenService.generateToken();
            log.info("Token granted: " + token);

            return ResponseEntity.ok(new ChatUserWithTokenDto(tempUser, token));

        } catch (BadUserException e) {
            log.info("Authenticated user doesn't exist!");
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ChatUserPasswordDto user) {
        log.info("Attempting to register new user: " + user.getUsername());

        try {
            ChatUser newUser = userService.saveUser(user);
            log.info("Registered new user: " + user.getUsername());
            return ResponseEntity.ok(new ChatUserPasswordDto(newUser));

        } catch (BadUserException exception) {
            log.info("Registration unsuccessful. User: " + user.getUsername()  + " already exists");
            return ResponseEntity.badRequest().build();
        }
    }
}
