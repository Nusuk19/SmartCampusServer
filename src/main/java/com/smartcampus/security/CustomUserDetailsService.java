package com.smartcampus.security;

import com.smartcampus.model.User;
import com.smartcampus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * 🆕 Сервіс для завантаження даних користувача
 */
@Service
public class CustomUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Завантажити користувача за ID
     */
    public CurrentUser loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new CurrentUser(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
