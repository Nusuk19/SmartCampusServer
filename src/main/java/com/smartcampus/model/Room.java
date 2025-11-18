package com.smartcampus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Room model для Spring Boot API
 *
 * КРИТИЧНО: JSON-поля збігаються з Android RoomEntity!
 *
 * ВИПРАВЛЕННЯ:
 * ✅ Додано Builder pattern
 * ✅ Валідація даних
 * ✅ Equals/HashCode
 */
public class Room {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("building")
    private String building;

    @JsonProperty("floor")
    private Integer floor;

    @JsonProperty("capacity")
    private Integer capacity;

    @JsonProperty("roomType")
    private String roomType;

    @JsonProperty("available")
    private boolean available;

    @JsonProperty("nfcTagId")
    private String nfcTagId;

    // ========== КОНСТРУКТОРИ ==========

    public Room() {
    }

    public Room(Long id, String name, String building, Integer floor,
                Integer capacity, String roomType, boolean available, String nfcTagId) {
        this.id = id;
        this.name = name;
        this.building = building;
        this.floor = floor;
        this.capacity = capacity;
        this.roomType = roomType;
        this.available = available;
        this.nfcTagId = nfcTagId;
    }

    // ========== BUILDER PATTERN ==========

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String building;
        private Integer floor;
        private Integer capacity;
        private String roomType;
        private boolean available = true;
        private String nfcTagId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder building(String building) {
            this.building = building;
            return this;
        }

        public Builder floor(Integer floor) {
            this.floor = floor;
            return this;
        }

        public Builder capacity(Integer capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder roomType(String roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder available(boolean available) {
            this.available = available;
            return this;
        }

        public Builder nfcTagId(String nfcTagId) {
            this.nfcTagId = nfcTagId;
            return this;
        }

        public Room build() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalStateException("Room name cannot be empty");
            }
            return new Room(id, name, building, floor, capacity, roomType, available, nfcTagId);
        }
    }

    // ========== ГЕТТЕРИ/СЕТТЕРИ ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getNfcTagId() {
        return nfcTagId;
    }

    public void setNfcTagId(String nfcTagId) {
        this.nfcTagId = nfcTagId;
    }

    // ========== UTILITY ==========

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", building='" + building + '\'' +
                ", floor=" + floor +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id != null && id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}