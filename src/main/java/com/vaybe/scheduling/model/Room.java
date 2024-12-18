package com.vaybe.scheduling.model;

import jakarta.persistence.*;

@Entity
public class Room {
    @Id
    private Long id;
    private String name;
    private int capacity;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String whoAmI() {
        return "id: " + id
                + "\nname: " + name
                + "\ncapacity: " + capacity;
    }
}
