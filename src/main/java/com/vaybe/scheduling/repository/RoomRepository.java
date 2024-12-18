package com.vaybe.scheduling.repository;

import com.vaybe.scheduling.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    // You can add custom query methods here if needed
}
