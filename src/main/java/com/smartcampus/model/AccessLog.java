package com.smartcampus.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 🆕 Entity для журналу доступу
 */
@Entity
@Table(name = "access_logs")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;  // Яким тегом відкрито

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private String action;  // UNLOCK, LOCK

    @Column(nullable = false)
    private String status;  // SUCCESS, DENIED, ERROR

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Constructors
    public AccessLog() {
        this.timestamp = LocalDateTime.now();
    }

    public AccessLog(Long userId, Long tagId, Long roomId, String action, String status, LocalDateTime timestamp) {
        this.userId = userId;
        this.tagId = tagId;
        this.roomId = roomId;
        this.action = action;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTagId() { return tagId; }
    public void setTagId(Long tagId) { this.tagId = tagId; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}