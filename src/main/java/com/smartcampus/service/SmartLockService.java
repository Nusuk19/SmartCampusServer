package com.smartcampus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервіс для керування електронними замками
 *
 * ВАРІАНТИ:
 * - MQTT (Eclipse Mosquitto)
 * - Bluetooth (через ESP32 gateway)
 * - REST API (Yale, August Smart Lock)
 */
@Service
public class SmartLockService {

    @Autowired
    private MqttService mqttService;

    /**
     * Відкрити замок
     */
    public void unlock(String lockId) {
        // Відправити MQTT команду
        String topic = "smartcampus/locks/" + lockId;
        String message = "UNLOCK";

        mqttService.publish(topic, message);

        System.out.println("🔓 Lock unlocked: " + lockId);
    }

    /**
     * Закрити замок
     */
    public void lock(String lockId) {
        String topic = "smartcampus/locks/" + lockId;
        String message = "LOCK";

        mqttService.publish(topic, message);

        System.out.println("🔒 Lock locked: " + lockId);
    }
}

// ========== ДОПОМІЖНІ КЛАСИ ==========

class AccessCheckResult {
    private boolean allowed;
    private String reason;

    public static AccessCheckResult allowed() {
        return new AccessCheckResult(true, null);
    }

    public static AccessCheckResult denied(String reason) {
        return new AccessCheckResult(false, reason);
    }

    private AccessCheckResult(boolean allowed, String reason) {
        this.allowed = allowed;
        this.reason = reason;
    }

    public boolean isAllowed() { return allowed; }
    public String getReason() { return reason; }
}

class UnlockResponse extends com.smartcampus.dto.UnlockResponse {
    private String status;
    private String roomName;
    private String message;

    public static UnlockResponse success(String roomName) {
        return new UnlockResponse("OK", roomName, "Door unlocked");
    }

    public static UnlockResponse denied(String message) {
        return new UnlockResponse("DENIED", null, message);
    }

    public static UnlockResponse occupied() {
        return new UnlockResponse("OCCUPIED", null, "Room occupied");
    }

    public static UnlockResponse error(String message) {
        return new UnlockResponse("ERROR", null, message);
    }

    private UnlockResponse(String status, String roomName, String message) {
        this.status = status;
        this.roomName = roomName;
        this.message = message;
    }

    // Getters
    public String getStatus() { return status; }
    public String getRoomName() { return roomName; }
    public String getMessage() { return message; }
}