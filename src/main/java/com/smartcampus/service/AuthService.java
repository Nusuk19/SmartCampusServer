package com.smartcampus.service;

import com.smartcampus.model.User;
import com.smartcampus.dto.AuthResponse;
import com.smartcampus.repository.UserRepository;
import com.smartcampus.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 🆕 Сервіс автентифікації
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private GoogleOAuthService googleOAuthService;

    /**
     * Реєстрація нового користувача
     */
    public AuthResponse register(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email вже зареєстрований");
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setName(name);
        user.setRole("STUDENT");
        user.setActive(true);

        user = userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken, toUserData(user));
    }

    /**
     * Вхід через Email + Password
     */
    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Невірний email або пароль"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Невірний email або пароль");
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException("Акаунт деактивовано");
        }

        user.setLastLoginAt(java.time.LocalDateTime.now());
        userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken, toUserData(user));
    }

    /**
     * Вхід через Google SSO
     */
    public AuthResponse loginWithGoogle(String googleIdToken) {
        GoogleOAuthService.GoogleUserInfo googleUser = googleOAuthService.verifyIdToken(googleIdToken);

        if (googleUser == null) {
            throw new IllegalArgumentException("Невірний Google ID Token");
        }

        User user = userRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(googleUser.getEmail());
                    newUser.setName(googleUser.getName());
                    newUser.setGoogleId(googleUser.getGoogleId());
                    newUser.setPhotoUrl(googleUser.getPhotoUrl());
                    newUser.setRole("STUDENT");
                    newUser.setActive(true);
                    return userRepository.save(newUser);
                });

        if (user.getGoogleId() == null) {
            user.setGoogleId(googleUser.getGoogleId());
            userRepository.save(user);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken, toUserData(user));
    }

    /**
     * Оновити Access Token
     */
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Невірний refresh token");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Користувача не знайдено"));

        String newAccessToken = jwtTokenProvider.createAccessToken(user);

        return new AuthResponse(newAccessToken, refreshToken, toUserData(user));
    }

    /**
     * 🔹 Утиліта для конвертації User → AuthResponse.UserData
     */
    private AuthResponse.UserData toUserData(User user) {
        AuthResponse.UserData data = new AuthResponse.UserData();
        data.setId(user.getId());
        data.setEmail(user.getEmail());
        data.setName(user.getName());
        data.setRole(user.getRole());
        data.setPhotoUrl(user.getPhotoUrl());
        return data;
    }
}
