package com.vaybe.scheduling.model;

import jakarta.persistence.*;

@Entity
public class Course {
    @Id
    private String id;
    private String level;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    public String whoAmI() {
        return "---------------"
                + "\nid: " + id
                + "\nlevel: " + level
                + "\nschoolClass: " + schoolClass + "\n";
    }

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

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }
}
