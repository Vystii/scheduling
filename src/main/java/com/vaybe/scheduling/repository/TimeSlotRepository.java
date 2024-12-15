package com.vaybe.scheduling.repository;

import com.vaybe.scheduling.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    Optional<TimeSlot> findByStartTimeAndEndTimeAndDayOfWeek(LocalDateTime startTime, LocalDateTime endTime,
            int dayOfWeek);
}
