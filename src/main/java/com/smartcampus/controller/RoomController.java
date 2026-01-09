package com.smartcampus.controller;

import com.smartcampus.model.Room;
import com.smartcampus.model.Schedule;
import com.smartcampus.service.RoomService;
import com.smartcampus.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * ✅ ОНОВЛЕНО: Controller з endpoints для розкладу
 */
@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private ScheduleService scheduleService;

    /**
     * GET /api/rooms - Отримати всі аудиторії
     */
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    /**
     * GET /api/rooms/{id} - Отримати аудиторію за ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/rooms/available - Отримати доступні аудиторії
     */
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(rooms);
    }

    /**
     * 🆕 GET /api/rooms/{id}/schedule - Отримати розклад аудиторії
     */
    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<Schedule>> getRoomSchedule(@PathVariable Long id) {
        List<Schedule> schedule = scheduleService.getRoomSchedule(id);
        return ResponseEntity.ok(schedule);
    }

    /**
     * 🆕 GET /api/rooms/{id}/schedule/today - Розклад на сьогодні
     */
    @GetMapping("/{id}/schedule/today")
    public ResponseEntity<List<Schedule>> getRoomScheduleToday(@PathVariable Long id) {
        List<Schedule> schedule = scheduleService.getRoomScheduleToday(id);
        return ResponseEntity.ok(schedule);
    }

    /**
     * 🆕 GET /api/rooms/{id}/schedule/current - Поточна пара
     */
    @GetMapping("/{id}/schedule/current")
    public ResponseEntity<Schedule> getCurrentClass(@PathVariable Long id) {
        Schedule currentClass = scheduleService.getCurrentClassInRoom(id);

        if (currentClass != null) {
            return ResponseEntity.ok(currentClass);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * 🆕 GET /api/rooms/{id}/schedule/next - Наступна пара
     */
    @GetMapping("/{id}/schedule/next")
    public ResponseEntity<Schedule> getNextClass(@PathVariable Long id) {
        Schedule nextClass = scheduleService.getNextClassInRoom(id);

        if (nextClass != null) {
            return ResponseEntity.ok(nextClass);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
