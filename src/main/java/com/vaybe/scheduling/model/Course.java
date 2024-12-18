package com.vaybe.scheduling.model;

import jakarta.persistence.*;

@Entity
public class Course {
    @Id
    private Long id;
    private String level;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    public String whoAmI() {
        return "---------------"
                + "\nid: " + id
                + "\nlevel: " + level + "\n";
    }

    // Getters and setters
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

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }
}
