package com.smartcampus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ✅ НАЙКРАЩЕ РІШЕННЯ: Окремий DTO для API response
 *
 * Переваги:
 * - Розділення JPA entities та API contracts
 * - Повний контроль над JSON структурою
 * - Немає проблем з серіалізацією
 */
public class TagResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("tagUid")
    private String tagUid;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("tagType")
    private String tagType;

    @JsonProperty("isActive")
    private boolean active;

    /**
     * ✅ ВИПРАВЛЕНО: Використовуємо long (timestamp в мілісекундах)
     */
    @JsonProperty("createdAt")
    private long createdAt;

    @JsonProperty("lastUsedAt")
    private long lastUsedAt;

    // Constructors
    public TagResponse() {}

    /**
     * Конвертувати NfcTag entity → TagResponse DTO
     */
    public static TagResponse fromEntity(com.smartcampus.model.NfcTag tag) {
        TagResponse response = new TagResponse();
        response.setId(tag.getId());
        response.setTagUid(tag.getTagUid());
        response.setUserId(tag.getUserId());
        response.setName(tag.getName());
        response.setTagType(tag.getTagType());
        response.setActive(tag.isActive());

        // ✅ Конвертуємо LocalDateTime → timestamp (мілісекунди)
        if (tag.getCreatedAt() != null) {
            response.setCreatedAt(
                    tag.getCreatedAt()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
            );
        }

        if (tag.getLastUsedAt() != null) {
            response.setLastUsedAt(
                    tag.getLastUsedAt()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
            );
        }

        return response;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTagUid() { return tagUid; }
    public void setTagUid(String tagUid) { this.tagUid = tagUid; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTagType() { return tagType; }
    public void setTagType(String tagType) { this.tagType = tagType; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(long lastUsedAt) { this.lastUsedAt = lastUsedAt; }
}