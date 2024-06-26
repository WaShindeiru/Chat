package org.example.chat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {

   @Order(value = Ordered.HIGHEST_PRECEDENCE)
   @Bean
   public SecurityFilterChain loginSecurityFilterChain(HttpSecurity http) throws Exception {
      http
              .securityMatcher("/login", "/register")
              .httpBasic(Customizer.withDefaults())
              .authorizeHttpRequests(auth -> auth
                      .requestMatchers("/register").permitAll()
                      .anyRequest().authenticated())

              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .csrf(AbstractHttpConfigurer::disable)
              .cors(c -> {
                 CorsConfigurationSource source = request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("*"));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                 };
                 c.configurationSource(source);
              });

      return http.build();
   }

   @Bean
   public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
      DefaultBearerTokenResolver resolver = new DefaultBearerTokenResolver();
      resolver.setAllowUriQueryParameter(true);

      http
              .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
              .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults())
                      .bearerTokenResolver(resolver))
              .csrf(AbstractHttpConfigurer::disable)
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .cors(c -> {
                 CorsConfigurationSource source = request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("*"));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                 };
                 c.configurationSource(source);
              });

      return http.build();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
