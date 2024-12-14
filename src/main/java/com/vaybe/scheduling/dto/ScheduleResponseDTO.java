package com.vaybe.scheduling.dto;

import com.vaybe.scheduling.model.Schedule;
import java.util.List;

public class ScheduleResponseDTO {
    private List<Schedule> schedule;
    private List<CourseDTO> unscheduled;

    // Getters and Setters
    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public List<CourseDTO> getUnscheduled() {
        return unscheduled;
    }

    public void setUnscheduled(List<CourseDTO> unscheduled) {
        this.unscheduled = unscheduled;
    }
}
