package com.smartcampus.repository;

import com.smartcampus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 🆕 Repository для користувачів
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Знайти користувача за email
     */
    Optional<User> findByEmail(String email);

    /**
     * Знайти користувача за Google ID
     */
    Optional<User> findByGoogleId(String googleId);

    /**
     * Перевірити чи email вже існує
     */
    boolean existsByEmail(String email);
}
