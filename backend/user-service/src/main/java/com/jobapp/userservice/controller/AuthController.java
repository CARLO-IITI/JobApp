package com.jobapp.userservice.controller;

import com.jobapp.common.dto.ApiResponse;
import com.jobapp.userservice.dto.AuthResponse;
import com.jobapp.userservice.dto.LoginRequest;
import com.jobapp.userservice.dto.RegisterRequest;
import com.jobapp.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        authService.validateToken(token);
        return ResponseEntity.ok(ApiResponse.success(true));
    }
}

