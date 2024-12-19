package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.RoomDTO;
import com.vaybe.scheduling.dto.RoomsRequestDTO;
import com.vaybe.scheduling.exception.ResourceNotFoundException;
import com.vaybe.scheduling.model.Room;
import com.vaybe.scheduling.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room addRoom(String id, String name, int capacity) {
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setCapacity(capacity);
        return roomRepository.save(room);
    }

    // Retrieve all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Create multiple rooms
    public List<Room> createRooms(List<Room> rooms, boolean deleteExistingRooms) {
        if (deleteExistingRooms) {
            roomRepository.deleteAll();
        }
        return roomRepository.saveAll(rooms);
    }

    // Create rooms from request DTO
    public List<Room> createRoomsFromRequest(RoomsRequestDTO roomsRequestDTO) {
        if (roomsRequestDTO == null) {
            return new ArrayList<>();
        }

        if (roomsRequestDTO.isShouldDeleteRooms()) {
            roomRepository.deleteAll(); // Clear all rooms
            roomRepository.flush(); // Ensure synchronization with the database
        }

        List<RoomDTO> roomDTOs = roomsRequestDTO.getData();
        if (roomDTOs == null) {
            return new ArrayList<>();
        }

        List<Room> rooms = new ArrayList<>();
        for (RoomDTO roomDTO : roomDTOs) {
            Room room = new Room();
            room.setId(roomDTO.getId());
            room.setName(roomDTO.getName());
            room.setCapacity(roomDTO.getCapacity());
            rooms.add(room);
        }
        return roomRepository.saveAll(rooms);
    }

    // Get a single room by ID
    public Room getRoomById(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + id));
    }

    // Update an existing room
    public Room updateRoom(String id, String name, int capacity) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + id));
        room.setName(name);
        room.setCapacity(capacity);
        return roomRepository.save(room);
    }

    // Delete a room by ID
    public void deleteRoom(String id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + id));
        roomRepository.delete(room);
    }
}
