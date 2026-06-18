package org.ecommerce.gatewayservice.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {
    private static final String SECRET =
            "my-secret-key-my-secret-key-my-secret-key";

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        SecretKey key =
                Keys.hmacShaKeyFor(
                        SECRET.getBytes(StandardCharsets.UTF_8));

        return NimbusReactiveJwtDecoder
                .withSecretKey(key)
                .build();

    }

}