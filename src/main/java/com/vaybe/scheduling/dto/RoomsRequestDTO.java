package com.vaybe.scheduling.dto;

import java.util.List;

public class RoomsRequestDTO {
    private boolean shouldDeleteRooms;
    private List<RoomDTO> data;

    // Getters and setters
    public boolean isShouldDeleteRooms() {
        return shouldDeleteRooms;
    }

    public void setShouldDeleteRooms(boolean shouldDeleteRooms) {
        this.shouldDeleteRooms = shouldDeleteRooms;
    }

    public List<RoomDTO> getData() {
        return data;
    }

    public void setData(List<RoomDTO> data) {
        this.data = data;
    }
}
