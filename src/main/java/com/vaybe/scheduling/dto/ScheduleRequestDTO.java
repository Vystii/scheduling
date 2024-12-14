package com.vaybe.scheduling.dto;

import java.util.List;

public class ScheduleRequestDTO {
    private int granularity;
    private List<CourseDTO> courses;
    private List<Integer> weekdays; // Number of granularity slots for each day
    private List<RoomDTO> rooms;

    // Getters and Setters
    public int getGranularity() {
        return granularity;
    }

    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }

    public List<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseDTO> courses) {
        this.courses = courses;
    }

    public List<Integer> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<Integer> weekdays) {
        this.weekdays = weekdays;
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }
}
