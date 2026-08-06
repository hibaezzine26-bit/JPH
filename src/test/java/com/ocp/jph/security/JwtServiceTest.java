package com.ocp.jph.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

    @Test
    void shouldGenerateAndValidateToken() {
        JwtService jwtService = new JwtService("test-secret-key-123456");
        UserDetails user = User.withUsername("test@example.com")
                .password("password")
                .authorities("ROLE_ADMINISTRATEUR")
                .build();

        String token = jwtService.generateToken(user);

        assertEquals("test@example.com", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, user));
    }
}
