package com.vaybe.scheduling.controller;

import com.vaybe.scheduling.dto.ScheduleRequestDTO;
import com.vaybe.scheduling.dto.ScheduleResponseDTO;
import com.vaybe.scheduling.model.Schedule;
import com.vaybe.scheduling.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService service;

    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return service.createSchedule(schedule);
    }

    @PutMapping("/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        return service.updateSchedule(id, schedule);
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        service.deleteSchedule(id);
    }

    @GetMapping
    public List<Schedule> getAllSchedules() {
        return service.getAllSchedules();
    }

    @PostMapping("/generate")
    public ScheduleResponseDTO generateSchedule(@RequestBody ScheduleRequestDTO request) {
        return service.generateSchedule(request);
    }
}
