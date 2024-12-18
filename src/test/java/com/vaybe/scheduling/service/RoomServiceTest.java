package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.RoomDTO;
import com.vaybe.scheduling.dto.RoomsRequestDTO;
import com.vaybe.scheduling.model.Room;
import com.vaybe.scheduling.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRoomsFromRequest() {
        RoomDTO roomDTO1 = new RoomDTO();
        roomDTO1.setName("Room A");
        roomDTO1.setCapacity(30);

        RoomDTO roomDTO2 = new RoomDTO();
        roomDTO2.setName("Room B");
        roomDTO2.setCapacity(50);

        RoomsRequestDTO roomsRequestDTO = new RoomsRequestDTO();
        roomsRequestDTO.setShouldDeleteRooms(true);
        roomsRequestDTO.setData(Arrays.asList(roomDTO1, roomDTO2));

        Room room1 = new Room();
        room1.setName("Room A");
        room1.setCapacity(30);

        Room room2 = new Room();
        room2.setName("Room B");
        room2.setCapacity(50);

        when(roomRepository.saveAll(any())).thenReturn(Arrays.asList(room1, room2));

        List<Room> createdRooms = roomService.createRoomsFromRequest(roomsRequestDTO);

        assertEquals(2, createdRooms.size());
        verify(roomRepository, times(1)).deleteAll();
        verify(roomRepository, times(1)).saveAll(any());
    }
}
