package com.smartcampus.service;

import com.smartcampus.model.Schedule;
import com.smartcampus.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 🆕 Сервіс для роботи з розкладом
 */
@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * Отримати розклад для аудиторії
     */
    public List<Schedule> getRoomSchedule(Long roomId) {
        return scheduleRepository.findByRoomId(roomId);
    }

    /**
     * Отримати розклад для аудиторії сьогодні
     */
    public List<Schedule> getRoomScheduleToday(Long roomId) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        return scheduleRepository.findByRoomId(roomId).stream()
                .filter(s -> s.getStartTime().isAfter(startOfDay) && s.getStartTime().isBefore(endOfDay))
                .toList();
    }

    /**
     * Отримати поточну пару в аудиторії
     */
    public Schedule getCurrentClassInRoom(Long roomId) {
        LocalDateTime now = LocalDateTime.now();
        List<Schedule> currentClasses = scheduleRepository.findByRoomIdAndTime(roomId, now);

        return currentClasses.isEmpty() ? null : currentClasses.get(0);
    }

    /**
     * Отримати наступну пару в аудиторії
     */
    public Schedule getNextClassInRoom(Long roomId) {
        LocalDateTime now = LocalDateTime.now();
        List<Schedule> todaySchedule = getRoomScheduleToday(roomId);

        return todaySchedule.stream()
                .filter(s -> s.getStartTime().isAfter(now))
                .min((s1, s2) -> s1.getStartTime().compareTo(s2.getStartTime()))
                .orElse(null);
    }

    /**
     * Отримати розклад користувача (викладача)
     */
    public List<Schedule> getUserSchedule(Long userId) {
        return scheduleRepository.findByUserId(userId);
    }

    /**
     * Отримати розклад користувача сьогодні
     */
    public List<Schedule> getUserScheduleToday(Long userId) {
        return scheduleRepository.findTodayScheduleForUser(userId);
    }

    /**
     * Створити новий запис в розкладі
     */
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    /**
     * Видалити запис з розкладу
     */
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }
}
