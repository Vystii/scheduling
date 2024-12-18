package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.RoomDTO;
import com.vaybe.scheduling.dto.RoomsRequestDTO;
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

    public Room addRoom(String name, int capacity) {
        Room room = new Room();
        room.setName(name);
        room.setCapacity(capacity);
        return roomRepository.save(room);
    }

    public List<Room> createRooms(List<Room> rooms, boolean deleteExistingRooms) {
        if (deleteExistingRooms) {
            roomRepository.deleteAll();
        }
        return roomRepository.saveAll(rooms);
    }

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
            System.out.println(room.whoAmI());
        }
        return roomRepository.saveAll(rooms);
    }

}
