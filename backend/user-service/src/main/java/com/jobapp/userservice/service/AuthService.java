package com.jobapp.userservice.service;

import com.jobapp.common.exception.BadRequestException;
import com.jobapp.common.exception.UnauthorizedException;
import com.jobapp.common.util.JwtUtil;
import com.jobapp.userservice.dto.AuthResponse;
import com.jobapp.userservice.dto.LoginRequest;
import com.jobapp.userservice.dto.RegisterRequest;
import com.jobapp.userservice.model.User;
import com.jobapp.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // Validate role
        User.UserRole userRole;
        try {
            userRole = User.UserRole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role. Must be CANDIDATE or RECRUITER");
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(userRole)
                .isActive(true)
                .emailVerified(false)
                .build();

        user = userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().toString())
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        // Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        // Check password
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        // Check if user is active
        if (!user.getIsActive()) {
            throw new UnauthorizedException("Account is deactivated");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().toString())
                .build();
    }

    public User validateToken(String token) {
        String email = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));

        if (!jwtUtil.validateToken(token, email)) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        return user;
    }
}

