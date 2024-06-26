package org.example.chat.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class RSAConfiguration {

   @Bean
   public KeyPair keyPairGenerator() throws NoSuchAlgorithmException {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      return keyPairGenerator.generateKeyPair();
   }

   @Bean
   public JwtEncoder jwtEncoder(KeyPair keyPair) {
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
      JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
      JWKSource<SecurityContext> temp = new ImmutableJWKSet<>(new JWKSet(jwk));
      return new NimbusJwtEncoder(temp);
   }

   @Bean
   public JwtDecoder jwtDecoder(KeyPair keyPair) {
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      return NimbusJwtDecoder.withPublicKey(publicKey).build();
   }
}