package com.ocp.jph.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                .requestMatchers("/api/utilisateurs/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/reportings/export").hasAnyRole("ADMINISTRATEUR", "CONSULTANT")
                .requestMatchers(HttpMethod.GET, "/api/reportings/**").hasAnyRole("ADMINISTRATEUR", "CONSULTANT")
                .requestMatchers(HttpMethod.GET, "/api/utilisateurs/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/utilisateurs/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/utilisateurs/me/mot-de-passe").authenticated()
                .requestMatchers("/api/utilisateurs/register").permitAll()
                .requestMatchers("/api/**").hasRole("ADMINISTRATEUR")
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
