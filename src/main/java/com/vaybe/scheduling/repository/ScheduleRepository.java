package com.vaybe.scheduling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vaybe.scheduling.model.Schedule;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourseIdIn(List<String> courseId);
}
