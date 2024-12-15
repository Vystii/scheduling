package com.vaybe.scheduling.controller;

import com.vaybe.scheduling.dto.ScheduleRequestDTO;
import com.vaybe.scheduling.dto.ScheduleResponseDTO;
import com.vaybe.scheduling.model.Settings;
import com.vaybe.scheduling.model.Schedule;
import com.vaybe.scheduling.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/generate")
    public ScheduleResponseDTO generateSchedule(@RequestBody ScheduleRequestDTO request,
            @RequestParam(defaultValue = "false") boolean deleteExistingSchedules) {
        return scheduleService.generateSchedule(request, deleteExistingSchedules);
    }

    @PostMapping("/settings")
    public void saveSettings(@RequestParam int granularity, @RequestParam int pauseDuration) {
        scheduleService.saveSettings(granularity, pauseDuration);
    }

    @GetMapping("/settings")
    public Settings getSettings() {
        return scheduleService.getSettings();
    }

    @PostMapping("/add")
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleService.createSchedule(schedule);
    }

    @PutMapping("/update/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule updatedSchedule) {
        return scheduleService.updateSchedule(id, updatedSchedule);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }

    @GetMapping("/all")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }
}
