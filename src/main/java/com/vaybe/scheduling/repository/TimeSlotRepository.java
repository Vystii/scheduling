package com.vaybe.scheduling.repository;

import com.vaybe.scheduling.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}
