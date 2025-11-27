package com.smartcampus.service;

import com.smartcampus.model.User;
import com.smartcampus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * 🆕 Сервіс для управління користувачами
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Отримати користувача за ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Оновити профіль
     */
    public User updateProfile(Long userId, String newName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setName(newName);
        return userRepository.save(user);
    }

    /**
     * Змінити пароль
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Перевірити старий пароль
        if (user.getPasswordHash() == null) {
            throw new IllegalArgumentException("Cannot change password for Google SSO users");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Встановити новий пароль
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}