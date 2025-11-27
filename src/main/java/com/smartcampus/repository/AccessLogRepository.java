package com.smartcampus.repository;

import com.smartcampus.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 🆕 Repository для журналу доступу
 */
@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    /**
     * Знайти всі записи користувача
     */
    List<AccessLog> findByUserId(Long userId);

    /**
     * Знайти записи користувача за період
     */
    List<AccessLog> findByUserIdAndTimestampBetween(Long userId,
                                                    LocalDateTime start,
                                                    LocalDateTime end);

    /**
     * Знайти всі записи для аудиторії
     */
    List<AccessLog> findByRoomId(Long roomId);

    /**
     * Знайти успішні відкриття дверей за період
     */
    @Query("SELECT a FROM AccessLog a WHERE a.status = 'SUCCESS' " +
            "AND a.timestamp BETWEEN :start AND :end " +
            "ORDER BY a.timestamp DESC")
    List<AccessLog> findSuccessfulAccessBetween(@Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);

    /**
     * Знайти заборонені спроби доступу
     */
    List<AccessLog> findByStatus(String status);

    /**
     * Статистика: кількість використань кожного тегу
     */
    @Query("SELECT a.tagId, COUNT(a) as count FROM AccessLog a " +
            "WHERE a.userId = :userId AND a.status = 'SUCCESS' " +
            "GROUP BY a.tagId")
    List<Object[]> getTagUsageStats(@Param("userId") Long userId);
}