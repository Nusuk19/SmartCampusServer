package com.smartcampus.repository;

import com.smartcampus.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository для аудиторій
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Знайти всі доступні аудиторії
     */
    List<Room> findByAvailable(boolean available);

    /**
     * 🆕 НОВИЙ: Знайти аудиторію за ID рідера
     */
    Optional<Room> findByNfcReaderId(String nfcReaderId);

    /**
     * 🆕 НОВИЙ: Знайти аудиторію за ID замка
     */
    Optional<Room> findByLockId(String lockId);

    /**
     * 🆕 НОВИЙ: Знайти аудиторії зайняті конкретним користувачем
     */
    List<Room> findByOccupiedBy(Long userId);
}