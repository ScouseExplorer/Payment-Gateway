package com.gateway.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * TEMPORARY Stage 1 security configuration.
 * 
 * spring-boot-starter-security defaults to denying all requests behind HTTP
 * Basic with a random per-boot password, which would make the API unusable
 * for local development and tests. This permits all requests so the payment
 * core API surface can be built and exercised end-to-end.
 * 
 * This MUST be replaced before anything resembling production use: Section 19
 * requires real authentication/authorisation (API keys and/or JWT). That work
 * is scoped to Stage 3 ("Production API") in Section 29 and should wire up
 * {@link ApiKeyFilter} / {@link JwtAuthenticationFilter} for real.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
