package com.vaybe.scheduling.controller;

import com.vaybe.scheduling.dto.ScheduleRequestDTO;
import com.vaybe.scheduling.dto.ScheduleResponseDTO;
import com.vaybe.scheduling.model.Room;
import com.vaybe.scheduling.model.Schedule;
import com.vaybe.scheduling.service.RoomService;
import com.vaybe.scheduling.service.ScheduleService;
import com.vaybe.scheduling.service.SchoolClassService;
import com.vaybe.scheduling.service.CourseService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private SchoolClassService schoolClassService;

    @Autowired
    private CourseService courseService; // Add CourseService

    @PostMapping("/generate")
    public ScheduleResponseDTO generateSchedule(@RequestBody ScheduleRequestDTO request,
            @RequestParam boolean deleteExistingSchedules) {
        // Call RoomService to handle room creation
        if (request.getRooms() != null) {
            roomService.createRoomsFromRequest(request.getRooms());
        }

        // Call SchoolClassService to handle school class creation
        if (request.getSchoolClasses() != null) {
            schoolClassService.createClassesFromRequest(request.getSchoolClasses());
        }

        // Call CourseService to handle course creation
        if (request.getCourses() != null && !request.getCourses().isEmpty()) {
            courseService.createCoursesFromRequest(request.getCourses());
        }

        // Generate schedule
        return scheduleService.generateSchedule(request, deleteExistingSchedules);
    }

    @PostMapping("/add")
    public Schedule addSchedule(@RequestBody Schedule schedule, @RequestParam String roomId) {
        // Fetch the room by its id
        Room room = roomService.getRoomById(roomId);
        schedule.setRoom(room);
        Schedule saved = scheduleService.addSchedule(schedule, roomId);
        return saved;
    }

    @PostMapping("/get-courses-schedules")
    public List<Schedule> getSchedulesByCourseIds(@RequestBody List<String> courseIds) {
        System.out.println(courseIds);
        return scheduleService.getSchedulesByCourseIds(courseIds);
    }

    @GetMapping("/get/{id}")
    public Optional<Schedule> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    @GetMapping("/get-all")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteScheduleById(@PathVariable Long id) {
        scheduleService.deleteScheduleById(id);
    }

    @PutMapping("/update/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        return scheduleService.updateSchedule(id, schedule);
    }
}
