package com.smartcampus.repository;

import com.smartcampus.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository для розкладу
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * Знайти розклад для конкретної аудиторії
     */
    List<Schedule> findByRoomId(Long roomId);

    /**
     * Знайти розклад для користувача (викладача)
     */
    List<Schedule> findByUserId(Long userId);

    /**
     * Знайти розклад для аудиторії та користувача
     */
    List<Schedule> findByUserIdAndRoomId(Long userId, Long roomId);

    /**
     * Знайти активні заняття в аудиторії в конкретний час
     */
    @Query("SELECT s FROM Schedule s WHERE s.roomId = :roomId " +
            "AND :time BETWEEN s.startTime AND s.endTime")
    List<Schedule> findByRoomIdAndTime(@Param("roomId") Long roomId,
                                       @Param("time") LocalDateTime time);

    /**
     * Знайти всі заняття користувача сьогодні
     */
    @Query("SELECT s FROM Schedule s WHERE s.userId = :userId " +
            "AND DATE(s.startTime) = CURRENT_DATE " +
            "ORDER BY s.startTime")
    List<Schedule> findTodayScheduleForUser(@Param("userId") Long userId);
}
