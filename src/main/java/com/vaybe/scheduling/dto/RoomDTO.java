package com.vaybe.scheduling.dto;

public class RoomDTO {
    private Long id; // Change to Long
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

    public String WhoAmI() {
        return "------------------"
                + "\nid: " + id
                + "\nname: " + name
                + "\ncapacity: " + capacity
                + "\n------------------";
    }
}
