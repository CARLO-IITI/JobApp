package com.jobapp.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Security configuration for User Service
 * - Allows anonymous access to auth endpoints
 * - Enables CORS
 * - Disables CSRF for REST API
 * - Uses stateless session management (for JWT)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API
            .csrf(csrf -> csrf.disable())
            
            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configure authorization
            .authorizeHttpRequests(auth -> auth
                // Allow anonymous access to auth endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Allow profile endpoints (JWT token will be validated in service layer)
                .requestMatchers("/api/profiles/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/experience/**").permitAll()
                // Allow actuator health check
                .requestMatchers("/actuator/health").permitAll()
                // All other requests need authentication
                .anyRequest().authenticated()
            )
            
            // Disable form login (we're using JWT)
            .formLogin(form -> form.disable())
            
            // Disable HTTP Basic
            .httpBasic(basic -> basic.disable())
            
            // Stateless session (no server-side sessions)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow requests from frontend
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:8080"
        ));
        
        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // How long the response from a preflight request can be cached
        configuration.setMaxAge(3600L);
        
        // Expose headers to the client
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization", "Content-Type"
        ));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}

