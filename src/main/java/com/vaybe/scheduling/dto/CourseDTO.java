package com.vaybe.scheduling.dto;

public class CourseDTO {
    private Long id;
    private String level;
    private String classId;
    private int expectedStudents; // Add this field

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getExpectedStudents() {
        return expectedStudents;
    }

    public void setExpectedStudents(int expectedStudents) {
        this.expectedStudents = expectedStudents;
    }
}
