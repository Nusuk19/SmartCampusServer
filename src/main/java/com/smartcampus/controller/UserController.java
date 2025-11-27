package com.smartcampus.controller;

import com.smartcampus.dto.*;
import com.smartcampus.model.User;
import com.smartcampus.security.CurrentUser;
import com.smartcampus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 🆕 Controller для профілю користувача
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * GET /api/users/me - Отримати свій профіль
     */
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(@AuthenticationPrincipal CurrentUser currentUser) {
        return userService.getUserById(currentUser.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/users/me - Оновити профіль
     */
    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody UpdateProfileRequest request) {
        try {
            User user = userService.updateProfile(currentUser.getId(), request.getName());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST /api/users/me/change-password - Змінити пароль
     */
    @PostMapping("/me/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(
                    currentUser.getId(),
                    request.getOldPassword(),
                    request.getNewPassword()
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}