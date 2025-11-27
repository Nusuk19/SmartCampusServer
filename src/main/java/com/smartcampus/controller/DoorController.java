package com.smartcampus.controller;

import com.smartcampus.dto.*;
import com.smartcampus.service.DoorAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 🆕 Controller для відкриття дверей
 */
@RestController
@RequestMapping("/api/doors")
@CrossOrigin(origins = "*")
public class DoorController {

    @Autowired
    private DoorAccessService doorAccessService;

    /**
     * POST /api/doors/unlock - Відкрити двері
     *
     * ЛОГІКА:
     * 1. Отримуємо tagUid (з фізичного або віртуального тегу)
     * 2. Знаходимо власника тегу
     * 3. Перевіряємо права доступу
     * 4. Відкриваємо замок
     */
    @PostMapping("/unlock")
    public ResponseEntity<UnlockResponse> unlockDoor(@RequestBody UnlockRequest request) {
        try {
            UnlockResponse response = doorAccessService.unlockDoor(
                    request.getTagUid(),
                    request.getReaderId()
            );

            // Повертаємо різні HTTP статуси залежно від результату
            switch (response.getStatus()) {
                case "OK":
                    return ResponseEntity.ok(response);
                case "DENIED":
                    return ResponseEntity.status(403).body(response);  // Forbidden
                case "OCCUPIED":
                    return ResponseEntity.status(409).body(response);  // Conflict
                default:
                    return ResponseEntity.status(500).body(response);  // Internal Server Error
            }
        } catch (IllegalArgumentException e) {
            UnlockResponse errorResponse = new UnlockResponse();
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}