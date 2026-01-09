package com.smartcampus.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 🆕 Entity для NFC тегів
 *
 * КОНЦЕПЦІЯ: Тег прив'язаний до користувача, а не до аудиторії
 */
@Entity
@Table(name = "nfc_tags")
public class NfcTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_uid", unique = true, nullable = false)
    private String tagUid;  // Унікальний UID (04:5E:2A:B2 або VT-04:5E:2A:B2)

    @Column(name = "user_id", nullable = false)
    private Long userId;  // Власник тегу

    @Column(name = "tag_type", nullable = false)
    private String tagType;  // PHYSICAL або VIRTUAL

    @Column(nullable = false)
    private String name;  // Назва тегу

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    // Constructors
    public NfcTag() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTagUid() { return tagUid; }
    public void setTagUid(String tagUid) { this.tagUid = tagUid; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTagType() { return tagType; }
    public void setTagType(String tagType) { this.tagType = tagType; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(LocalDateTime lastUsedAt) { this.lastUsedAt = lastUsedAt; }
}