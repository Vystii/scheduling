package com.vaybe.scheduling.dto;

import java.util.List;

public class ScheduleRequestDTO {
    private int granularity;
    private List<CourseDTO> courses;
    private List<Integer> weekdays;
    private RoomsRequestDTO rooms; // Include RoomsRequestDTO
    private SchoolClassesRequestDTO schoolClasses; // Include SchoolClassesRequestDTO

    // Getters and setters
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

    public RoomsRequestDTO getRooms() {
        return rooms;
    }

    public void setRooms(RoomsRequestDTO rooms) {
        this.rooms = rooms;
    }

    public SchoolClassesRequestDTO getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(SchoolClassesRequestDTO schoolClasses) {
        this.schoolClasses = schoolClasses;
    }
}
