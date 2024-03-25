package org.example.chat.security;

import lombok.RequiredArgsConstructor;
import org.example.chat.persistence.Authority;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class SecurityAuthority implements GrantedAuthority {

   private final Authority authority;

   @Override
   public String getAuthority() {
      return authority.getName();
   }
}
