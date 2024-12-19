package com.vaybe.scheduling.controller;

import com.vaybe.scheduling.model.Room;
import com.vaybe.scheduling.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return roomService.addRoom(room.getId(), room.getName(), room.getCapacity());
    }

    @PostMapping("/create-multiple")
    public List<Room> createRooms(@RequestBody List<Room> rooms, @RequestParam boolean deleteExistingRooms) {
        return roomService.createRooms(rooms, deleteExistingRooms);
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable String id) {
        return roomService.getRoomById(id);
    }

    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable String id, @RequestBody Room roomDetails) {
        return roomService.updateRoom(id, roomDetails.getName(), roomDetails.getCapacity());
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(id);
    }
}
