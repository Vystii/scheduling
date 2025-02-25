package com.vaybe.scheduling.model;

import jakarta.persistence.*;

@Entity
public class Room {
    @Id
    private String id;
    private String name;
    private int capacity;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
