package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.CourseDTO;
import com.vaybe.scheduling.dto.RoomDTO;
import com.vaybe.scheduling.dto.ScheduleRequestDTO;
import com.vaybe.scheduling.dto.ScheduleResponseDTO;
import com.vaybe.scheduling.model.Schedule;
import com.vaybe.scheduling.model.TimeSlot;
import com.vaybe.scheduling.repository.ScheduleRepository;
import com.vaybe.scheduling.repository.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @InjectMocks
    private ScheduleService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateSchedule() {
        // Mock repository save behavior
        when(scheduleRepository.save(org.mockito.Mockito.any(Schedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(timeSlotRepository.save(org.mockito.Mockito.any(TimeSlot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Test data setup
        RoomDTO room1 = new RoomDTO();
        room1.setId("Room1");
        room1.setCapacity(30);

        RoomDTO room2 = new RoomDTO();
        room2.setId("Room2");
        room2.setCapacity(50);

        CourseDTO course1 = new CourseDTO();
        course1.setId(1L);
        course1.setLevel("Beginner");
        course1.setClassId("ClassA");
        course1.setExpectedStudents(25);

        CourseDTO course2 = new CourseDTO();
        course2.setId(2L);
        course2.setLevel("Intermediate");
        course2.setClassId("ClassA");
        course2.setExpectedStudents(40);

        CourseDTO course3 = new CourseDTO();
        course3.setId(3L);
        course3.setLevel("Advanced");
        course3.setClassId("ClassB");
        course3.setExpectedStudents(55);

        ScheduleRequestDTO request = new ScheduleRequestDTO();
        request.setGranularity(180); // 3 hours
        request.setCourses(Arrays.asList(course1, course2, course3));
        request.setWeekdays(Collections.singletonList(4)); // 4 slots per day
        request.setRooms(Arrays.asList(room1, room2));

        // Generate schedule
        ScheduleResponseDTO response = service.generateSchedule(request);

        // Assertions
        assertThat(response).isNotNull();
        assertThat(response.getSchedule()).isNotEmpty();
        assertThat(response.getUnscheduled()).contains(course3); // Course 3 should be unscheduled due to capacity

        // Verify each course is assigned correctly
        List<Schedule> schedules = response.getSchedule();
        assertThat(schedules).hasSize(2);

        Schedule schedule1 = schedules.get(0);
        assertThat(schedule1.getTitle()).isEqualTo("Course 1");
        assertThat(schedule1.getDescription()).contains("Level: Beginner");
        assertThat(schedule1.getRoomId()).isNotNull();
        assertThat(schedule1.getClassId()).isEqualTo("ClassA");
        assertThat(schedule1.getCourseId()).isEqualTo(1L);
        assertThat(schedule1.getTimeSlot()).isNotNull();
        assertThat(schedule1.getTimeSlot().getDayOfWeek()).isEqualTo(1); // Assuming Monday
        assertThat(schedule1.getTimeSlot().getStartTime()).isNotNull();
        assertThat(schedule1.getTimeSlot().getEndTime()).isNotNull();

        Schedule schedule2 = schedules.get(1);
        assertThat(schedule2.getTitle()).isEqualTo("Course 2");
        assertThat(schedule2.getDescription()).contains("Level: Intermediate");
        assertThat(schedule2.getRoomId()).isNotNull();
        assertThat(schedule2.getClassId()).isEqualTo("ClassA");
        assertThat(schedule2.getCourseId()).isEqualTo(2L);
        assertThat(schedule2.getTimeSlot()).isNotNull();
        assertThat(schedule2.getTimeSlot().getDayOfWeek()).isEqualTo(1); // Assuming Monday
        assertThat(schedule2.getTimeSlot().getStartTime()).isNotNull();
        assertThat(schedule2.getTimeSlot().getEndTime()).isNotNull();

        // Verify course3 is correctly unscheduled due to capacity
        assertThat(response.getUnscheduled()).contains(course3);
    }

    // Additional tests for other scenarios can be added here
}
