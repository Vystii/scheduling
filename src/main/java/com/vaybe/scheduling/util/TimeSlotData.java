package com.vaybe.scheduling.util;

import com.vaybe.scheduling.model.Course;
import com.vaybe.scheduling.model.Room;

import java.time.LocalDateTime;

public class TimeSlotData {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean available;
    // private int pauseDuration;

    public TimeSlotData(LocalDateTime startTime, LocalDateTime endTime, int pauseDuration) {
        this.startTime = startTime;
        this.endTime = endTime;
        // this.pauseDuration = pauseDuration;
        this.available = true;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean canAccommodate(int granularity) {
        return !endTime.isBefore(startTime.plusMinutes(granularity));
    }

    public boolean assignCourse(Course course, Room room, int granularity) {
        if (isAvailable() && canAccommodate(granularity)) {
            // Assign the course to the timeslot
            this.available = false;
            return true;
        }
        return false;
    }
}
