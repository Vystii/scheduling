package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.CourseDTO;
import com.vaybe.scheduling.dto.RoomDTO;
import com.vaybe.scheduling.dto.ScheduleRequestDTO;
import com.vaybe.scheduling.dto.ScheduleResponseDTO;
import com.vaybe.scheduling.model.Schedule;
import com.vaybe.scheduling.model.TimeSlot;
import com.vaybe.scheduling.repository.ScheduleRepository;
import com.vaybe.scheduling.repository.TimeSlotRepository;
import com.vaybe.scheduling.repository.SettingsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;

class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @Mock
    private SettingsRepository settingsRepository;
    @InjectMocks
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateScheduleWithDeletion() {
        // Mock repository save behavior
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(invocation -> invocation.getArgument(0));

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
        ScheduleResponseDTO response = scheduleService.generateSchedule(request, true);

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

        // Ensure that if course 3 was scheduled, its details would also be correct
        if (!response.getUnscheduled().contains(course3)) {
            Schedule schedule3 = schedules.stream().filter(s -> s.getCourseId().equals(course3.getId())).findFirst()
                    .orElse(null);
            assertThat(schedule3).isNotNull();
            assertThat(schedule3.getTitle()).isEqualTo("Course 3");
            assertThat(schedule3.getDescription()).contains("Level: Advanced");
            assertThat(schedule3.getRoomId()).isNotNull();
            assertThat(schedule3.getClassId()).isEqualTo("ClassB");
            assertThat(schedule3.getCourseId()).isEqualTo(3L);
            assertThat(schedule3.getTimeSlot()).isNotNull();
            assertThat(schedule3.getTimeSlot().getDayOfWeek()).isEqualTo(1); // Assuming Monday
            assertThat(schedule3.getTimeSlot().getStartTime()).isNotNull();
            assertThat(schedule3.getTimeSlot().getEndTime()).isNotNull();
        }
    }

    @Test
    void testGenerateScheduleWithoutDeletion() {
        // Mock repository save behavior
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(invocation -> invocation.getArgument(0));

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
        ScheduleResponseDTO response = scheduleService.generateSchedule(request, false);

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

        // Ensure that if course 3 was scheduled, its details would also be correct
        if (!response.getUnscheduled().contains(course3)) {
            Schedule schedule3 = schedules.stream().filter(s -> s.getCourseId().equals(course3.getId())).findFirst()
                    .orElse(null);
            assertThat(schedule3).isNotNull();
            assertThat(schedule3.getTitle()).isEqualTo("Course 3");
            assertThat(schedule3.getDescription()).contains("Level: Advanced");
            assertThat(schedule3.getRoomId()).isNotNull();
            assertThat(schedule3.getClassId()).isEqualTo("ClassB");
            assertThat(schedule3.getCourseId()).isEqualTo(3L);
            assertThat(schedule3.getTimeSlot()).isNotNull();
            assertThat(schedule3.getTimeSlot().getDayOfWeek()).isEqualTo(1); // Assuming Monday
            assertThat(schedule3.getTimeSlot().getStartTime()).isNotNull();
            assertThat(schedule3.getTimeSlot().getEndTime()).isNotNull();
        }
    }

    @Test
    void testNoTimeSlotOverlap() {
        // Mock repository save behavior
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(timeSlotRepository.findByStartTimeAndEndTimeAndDayOfWeek(any(LocalDateTime.class),
                any(LocalDateTime.class), anyInt()))
                .thenAnswer(invocation -> Optional.empty());

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

        ScheduleRequestDTO request = new ScheduleRequestDTO();
        request.setGranularity(120); // 2 hours
        request.setCourses(Arrays.asList(course1, course2));
        request.setWeekdays(Collections.singletonList(8)); // 8 slots per day (16 hours)
        request.setRooms(Arrays.asList(room1, room2));

        // Call generateSchedule twice without deletions
        scheduleService.generateSchedule(request, false);
        scheduleService.generateSchedule(request, false);

        // Verify that no time slots overlap
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        for (int i = 0; i < timeSlots.size(); i++) {
            TimeSlot timeSlot = timeSlots.get(i);
            for (int j = i + 1; j < timeSlots.size(); j++) {
                TimeSlot otherTimeSlot = timeSlots.get(j);
                boolean isOverlap = timeSlot.getDayOfWeek() == otherTimeSlot.getDayOfWeek()
                        && timeSlot.getStartTime().isBefore(otherTimeSlot.getEndTime())
                        && otherTimeSlot.getStartTime().isBefore(timeSlot.getEndTime());
                assertThat(isOverlap).isFalse();
            }
        }
    }

}
