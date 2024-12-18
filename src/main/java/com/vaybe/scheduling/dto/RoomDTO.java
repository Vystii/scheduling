package com.vaybe.scheduling.dto;

public class RoomDTO {
    private String id; // Change to Long
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

    public String WhoAmI() {
        return "------------------"
                + "\nid: " + id
                + "\nname: " + name
                + "\ncapacity: " + capacity
                + "\n------------------";
    }
}
