package org.example.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.chat.dto.ChatUserDto;
import org.example.chat.persistence.ChatUser;
import org.example.chat.service.ChatUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

   private final ChatUserService userService;

   @GetMapping()
   public ResponseEntity<?> whoAmI() {
      JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
      ChatUser user = userService.getUserByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("Authenticated user doesn't exists"));
      return ResponseEntity.ok(new ChatUserDto(user));
   }
}
