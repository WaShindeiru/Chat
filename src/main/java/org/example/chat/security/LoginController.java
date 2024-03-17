package org.example.chat.security;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
@AllArgsConstructor
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final TokenService tokenService;

    @PostMapping("/login")
    public String login() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Requesting token for: " + auth.getName());
        String token = tokenService.generateToken();
        log.info("Token granted: " + token);
        return token;
    }
}
