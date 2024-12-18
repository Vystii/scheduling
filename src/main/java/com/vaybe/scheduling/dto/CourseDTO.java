package com.vaybe.scheduling.dto;

public class CourseDTO {
    private String id;
    private String level;
    private String schoolClassId;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(String schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public String whoAmI() {
        return "-------------------"
                + "\nid: " + id
                + "\nlevel: " + level
                + "\nclass: " + schoolClassId + "\n";
    }
}
