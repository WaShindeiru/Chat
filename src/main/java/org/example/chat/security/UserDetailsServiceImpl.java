package org.example.chat.security;

import lombok.AllArgsConstructor;
import org.example.chat.persistence.ChatUser;
import org.example.chat.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<ChatUser> temp = userRepository.findByUsername(username);
      return temp.map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("User: " + username + "not found"));
   }
}
