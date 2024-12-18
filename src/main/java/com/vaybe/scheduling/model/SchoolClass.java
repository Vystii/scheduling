package com.vaybe.scheduling.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SchoolClass {

    @Id
    private String name; // Using name as the primary key

    private int numberOfStudents;
    private String level;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return name;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
