package org.example.chat.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenService {

   private final JwtEncoder encoder;

   public String generateToken() {
      var auth = SecurityContextHolder.getContext().getAuthentication();
      Instant now = Instant.now();
      String scope = auth.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.joining(" "));

      JwtClaimsSet claims = JwtClaimsSet.builder()
              .issuer("Chat application")
              .issuedAt(now)
              .expiresAt(now.plus(1, ChronoUnit.HOURS))
              .subject(auth.getName())
              .claim("scope", scope)
              .build();

      return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
   }
}
