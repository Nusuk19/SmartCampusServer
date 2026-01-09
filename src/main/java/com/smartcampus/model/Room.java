package com.smartcampus.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 🔄 Entity для аудиторій (ОНОВЛЕНО)
 */
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String building;

    private Integer floor;

    private Integer capacity;

    @Column(nullable = false)
    private boolean available = true;

    // 🆕 НОВИЙ: ID рідера на дверях
    @Column(name = "nfc_reader_id", unique = true)
    private String nfcReaderId;  // Наприклад: "R305" (Reader-305)

    // 🆕 НОВИЙ: ID електронного замка
    @Column(name = "lock_id")
    private String lockId;  // Наприклад: "LOCK-305"

    // 🆕 НОВИЙ: Хто зайняв аудиторію
    @Column(name = "occupied_by")
    private Long occupiedBy;  // user_id

    // 🆕 НОВИЙ: Коли зайняли
    @Column(name = "occupied_at")
    private LocalDateTime occupiedAt;

    // Constructors
    public Room() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getNfcReaderId() { return nfcReaderId; }
    public void setNfcReaderId(String nfcReaderId) { this.nfcReaderId = nfcReaderId; }

    public String getLockId() { return lockId; }
    public void setLockId(String lockId) { this.lockId = lockId; }

    public Long getOccupiedBy() { return occupiedBy; }
    public void setOccupiedBy(Long occupiedBy) { this.occupiedBy = occupiedBy; }

    public LocalDateTime getOccupiedAt() { return occupiedAt; }
    public void setOccupiedAt(LocalDateTime occupiedAt) { this.occupiedAt = occupiedAt; }
}