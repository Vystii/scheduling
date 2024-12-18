package com.vaybe.scheduling.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Settings {
    @Id
    private Long id;
    private int granularity;
    private int pauseDuration;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String toString() {
        return "--------------"
                + "\nid: " + id
                + "\ngranuality" + granularity
                + "\npause_duration" + pauseDuration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGranularity() {
        return granularity;
    }

    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }

    public int getPauseDuration() {
        return pauseDuration;
    }

    public void setPauseDuration(int pauseDuration) {
        this.pauseDuration = pauseDuration;
    }
}
