package com.smartcampus.controller;

import com.smartcampus.model.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller для роботи з аудиторіями
 *
 * ВИПРАВЛЕННЯ:
 * ✅ Використання Builder pattern
 * ✅ Правильні HTTP коди
 * ✅ Логування
 * ✅ Error handling
 */
@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private static final String TAG = "[RoomController]";

    // In-memory mock data
    private static final Map<Long, Room> ROOMS = new LinkedHashMap<>();

    static {
        // ✅ ВИПРАВЛЕНО: Використання Builder
        ROOMS.put(1L, Room.builder()
                .id(1L)
                .name("305")
                .building("Корпус 5")
                .floor(3)
                .capacity(30)
                .roomType("LECTURE")
                .available(true)
                .nfcTagId("NFC001")
                .build()
        );

        ROOMS.put(2L, Room.builder()
                .id(2L)
                .name("306")
                .building("Корпус 5")
                .floor(3)
                .capacity(25)
                .roomType("LAB")
                .available(false)  // Зайнята
                .nfcTagId("NFC002")
                .build()
        );

        ROOMS.put(3L, Room.builder()
                .id(3L)
                .name("401")
                .building("Корпус 5")
                .floor(4)
                .capacity(40)
                .roomType("COMPUTER")
                .available(true)
                .nfcTagId("NFC003")
                .build()
        );

        ROOMS.put(4L, Room.builder()
                .id(4L)
                .name("210")
                .building("Корпус 5")
                .floor(2)
                .capacity(20)
                .roomType("LECTURE")
                .available(true)
                .nfcTagId("NFC004")
                .build()
        );

        System.out.println(TAG + " ✅ Initialized " + ROOMS.size() + " rooms");
    }

    /**
     * GET /api/rooms
     * Отримати всі аудиторії
     */
    @GetMapping
    public ResponseEntity<Collection<Room>> getAllRooms() {
        System.out.println(TAG + " 📥 GET /api/rooms");
        return ResponseEntity.ok(ROOMS.values());
    }

    /**
     * GET /api/rooms/available
     * Отримати тільки вільні аудиторії
     */
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        System.out.println(TAG + " 📥 GET /api/rooms/available");

        List<Room> available = ROOMS.values().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());

        System.out.println(TAG + " ✅ Returning " + available.size() + " available rooms");
        return ResponseEntity.ok(available);
    }

    /**
     * GET /api/rooms/{id}
     * Отримати конкретну аудиторію
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        System.out.println(TAG + " 📥 GET /api/rooms/" + id);

        Room room = ROOMS.get(id);
        if (room == null) {
            System.out.println(TAG + " ❌ Room not found: " + id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Room not found", "id", id));
        }

        return ResponseEntity.ok(room);
    }

    /**
     * POST /api/rooms/open/{nfcTagId}
     * Відкрити аудиторію через NFC
     */
    @PostMapping("/open/{nfcTagId}")
    public ResponseEntity<Map<String, String>> openRoomByNfc(@PathVariable String nfcTagId) {
        System.out.println(TAG + " 🔓 POST /api/rooms/open/" + nfcTagId);

        for (Room room : ROOMS.values()) {
            if (nfcTagId.equals(room.getNfcTagId())) {
                if (room.isAvailable()) {
                    room.setAvailable(false);
                    System.out.println(TAG + " ✅ Room " + room.getName() + " opened");

                    return ResponseEntity.ok(Map.of(
                            "status", "OK",
                            "message", "Room " + room.getName() + " opened successfully",
                            "roomId", String.valueOf(room.getId()),
                            "roomName", room.getName()
                    ));
                } else {
                    System.out.println(TAG + " ⚠️ Room " + room.getName() + " already occupied");

                    return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(Map.of(
                                    "status", "CONFLICT",
                                    "message", "Room already occupied",
                                    "roomId", String.valueOf(room.getId())
                            ));
                }
            }
        }

        System.out.println(TAG + " ❌ NFC tag not found: " + nfcTagId);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", "NOT_FOUND",
                        "message", "NFC tag not linked to any room",
                        "nfcTagId", nfcTagId
                ));
    }

    /**
     * PATCH /api/rooms/{id}/availability
     * Змінити статус доступності
     */
    @PatchMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean isAvailable) {

        System.out.println(TAG + " 🔄 PATCH /api/rooms/" + id + "/availability?isAvailable=" + isAvailable);

        Room room = ROOMS.get(id);
        if (room == null) {
            System.out.println(TAG + " ❌ Room not found: " + id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Room not found", "id", id));
        }

        room.setAvailable(isAvailable);
        System.out.println(TAG + " ✅ Room " + room.getName() + " availability → " + isAvailable);

        return ResponseEntity.ok(room);
    }

    /**
     * POST /api/rooms
     * Створити нову аудиторію (для майбутнього)
     */
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        System.out.println(TAG + " 📝 POST /api/rooms: " + room);

        Long newId = ROOMS.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L) + 1;

        room.setId(newId);
        ROOMS.put(newId, room);

        System.out.println(TAG + " ✅ Room created: " + newId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(room);
    }

    /**
     * DELETE /api/rooms/{id}
     * Видалити аудиторію
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        System.out.println(TAG + " 🗑️ DELETE /api/rooms/" + id);

        Room removed = ROOMS.remove(id);
        if (removed == null) {
            System.out.println(TAG + " ❌ Room not found: " + id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Room not found", "id", id));
        }

        System.out.println(TAG + " ✅ Room deleted: " + id);
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "Room deleted",
                "id", id
        ));
    }

    /**
     * GET /api/rooms/health
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        System.out.println(TAG + " ❤️ GET /api/rooms/health");

        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "rooms", String.valueOf(ROOMS.size()),
                "timestamp", String.valueOf(System.currentTimeMillis())
        ));
    }

    /**
     * GET /api/rooms/stats
     * Статистика (для дебагу)
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        System.out.println(TAG + " 📊 GET /api/rooms/stats");

        long available = ROOMS.values().stream()
                .filter(Room::isAvailable)
                .count();

        long occupied = ROOMS.size() - available;

        return ResponseEntity.ok(Map.of(
                "total", ROOMS.size(),
                "available", available,
                "occupied", occupied,
                "occupancyRate", ROOMS.isEmpty() ? 0 : (occupied * 100.0 / ROOMS.size())
        ));
    }
}