package com.vaybe.scheduling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vaybe.scheduling.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
