package com.smartcampus.controller;

import com.smartcampus.dto.*;
import com.smartcampus.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 🆕 Controller для автентифікації
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")  // Для тестування (в продакшені обмежити)
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /api/auth/register - Реєстрація
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(
                    request.getEmail(),
                    request.getPassword(),
                    request.getName()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST /api/auth/login - Вхід через Email + Password
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(
                    request.getEmail(),
                    request.getPassword()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).build();  // Unauthorized
        }
    }

    /**
     * POST /api/auth/google - Вхід через Google SSO
     */
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody GoogleTokenRequest request) {
        try {
            AuthResponse response = authService.loginWithGoogle(request.getIdToken());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * POST /api/auth/refresh - Оновити Access Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * POST /api/auth/logout - Вийти (опціонально)
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // У JWT немає серверного стану, тому просто повертаємо OK
        // Клієнт сам видалить токен
        return ResponseEntity.ok().build();
    }
}