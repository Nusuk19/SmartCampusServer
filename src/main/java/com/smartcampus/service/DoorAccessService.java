package com.smartcampus.service;

import com.smartcampus.model.*;
import com.smartcampus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 🔄 ОНОВЛЕНО: Сервіс контролю доступу
 */
@Service
public class DoorAccessService {

    @Autowired
    private NfcTagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    private SmartLockService smartLockService;

    /**
     * 🔄 ГОЛОВНИЙ МЕТОД: Відкрити двері через NFC тег
     *
     * ЛОГІКА:
     * 1. Знайти тег за UID
     * 2. Перевірити чи активний
     * 3. Знайти власника тегу
     * 4. Визначити аудиторію (за readerId)
     * 5. Перевірити права доступу
     * 6. Відкрити замок
     * 7. Оновити статус кімнати
     * 8. Логування
     */
    public UnlockResponse unlockDoor(String tagUid, String readerId) {
        LocalDateTime now = LocalDateTime.now();

        // 1. Знайти тег
        NfcTag tag = tagRepository.findByTagUid(tagUid)
                .orElseThrow(() -> new IllegalArgumentException("Unknown tag"));

        // 2. Перевірити чи активний
        if (!tag.isActive()) {
            return UnlockResponse.denied("Tag deactivated");
        }

        // 3. Знайти власника
        User user = userRepository.findById(tag.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 4. Визначити аудиторію
        Room room;
        if (readerId != null && !readerId.isEmpty()) {
            room = roomRepository.findByNfcReaderId(readerId)
                    .orElseThrow(() -> new IllegalArgumentException("Reader not found"));
        } else {
            throw new IllegalArgumentException("Reader ID required");
        }

        // 5. Перевірити права доступу
        AccessCheckResult accessCheck = checkAccess(user, room, now);
        if (!accessCheck.isAllowed()) {
            logAccess(user.getId(), tag.getId(), room.getId(), "UNLOCK", "DENIED", now);
            return UnlockResponse.denied(accessCheck.getReason());
        }

        // 6. Перевірити чи кімната вільна
        if (!room.isAvailable()) {
            logAccess(user.getId(), tag.getId(), room.getId(), "UNLOCK", "OCCUPIED", now);
            return UnlockResponse.occupied();
        }

        // 7. Відкрити замок
        try {
            smartLockService.unlock(room.getLockId());
        } catch (Exception e) {
            logAccess(user.getId(), tag.getId(), room.getId(), "UNLOCK", "ERROR", now);
            return UnlockResponse.error("Lock error: " + e.getMessage());
        }

        // 8. Оновити статус кімнати
        room.setAvailable(false);
        room.setOccupiedBy(user.getId());
        room.setOccupiedAt(now);
        roomRepository.save(room);

        // 9. Логування
        logAccess(user.getId(), tag.getId(), room.getId(), "UNLOCK", "SUCCESS", now);

        // 10. Оновити lastUsedAt тегу
        tag.setLastUsedAt(now);
        tagRepository.save(tag);

        return UnlockResponse.success(room.getName());
    }

    /**
     * Перевірити права доступу
     */
    private AccessCheckResult checkAccess(User user, Room room, LocalDateTime now) {
        // Адміністратори мають доступ завжди
        if ("ADMIN".equals(user.getRole())) {
            return AccessCheckResult.allowed();
        }

        // Викладачі можуть відкривати свої аудиторії
        if ("PROFESSOR".equals(user.getRole())) {
            List<Schedule> schedules = scheduleRepository.findByUserIdAndRoomId(
                    user.getId(), room.getId()
            );

            for (Schedule schedule : schedules) {
                if (now.isAfter(schedule.getStartTime().minusMinutes(10)) &&
                        now.isBefore(schedule.getEndTime())) {
                    return AccessCheckResult.allowed();
                }
            }
        }

        // Студенти можуть відкривати якщо є заняття
        if ("STUDENT".equals(user.getRole())) {
            List<Schedule> schedules = scheduleRepository.findByRoomIdAndTime(
                    room.getId(), now
            );

            // Перевірити чи студент є в групі цього заняття
            for (Schedule schedule : schedules) {
                if (isStudentInClass(user, schedule)) {
                    return AccessCheckResult.allowed();
                }
            }
        }

        return AccessCheckResult.denied("No access rights");
    }

    /**
     * Перевірити чи студент в групі заняття
     */
    private boolean isStudentInClass(User student, Schedule schedule) {
        // TODO: Реалізувати логіку перевірки групи студента
        // Наприклад: student.getGroupId() == schedule.getGroupId()
        return true; // Тимчасово
    }

    /**
     * Логування доступу
     */
    private void logAccess(Long userId, Long tagId, Long roomId,
                           String action, String status, LocalDateTime timestamp) {
        AccessLog log = new AccessLog();
        log.setUserId(userId);
        log.setTagId(tagId);
        log.setRoomId(roomId);
        log.setAction(action);
        log.setStatus(status);
        log.setTimestamp(timestamp);
        accessLogRepository.save(log);
    }
}