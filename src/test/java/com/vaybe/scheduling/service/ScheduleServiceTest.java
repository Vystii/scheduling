package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.CourseDTO;
import com.vaybe.scheduling.dto.RoomDTO;
import com.vaybe.scheduling.dto.RoomsRequestDTO;
import com.vaybe.scheduling.dto.ScheduleRequestDTO;
import com.vaybe.scheduling.dto.ScheduleResponseDTO;
import com.vaybe.scheduling.dto.SchoolClassDTO;
import com.vaybe.scheduling.dto.SchoolClassesRequestDTO;
import com.vaybe.scheduling.model.*;
import com.vaybe.scheduling.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private SettingsRepository settingsRepository;

    @Mock
    private SchoolClassService schoolClassService;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    public void testGenerateSchedule() {
        // Given
        Room room = new Room();
        room.setId(1L);
        room.setName("Room A");
        room.setCapacity(30);

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("Class 1");
        schoolClass.setNumberOfStudents(25);
        schoolClass.setLevel("L1");

        Course course = new Course();
        course.setId(1L);
        course.setLevel("Beginner");
        course.setSchoolClass(schoolClass);

        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(schoolClassRepository.save(any(SchoolClass.class))).thenReturn(schoolClass);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        when(roomRepository.findAll()).thenReturn(Arrays.asList(room));
        when(schoolClassRepository.findAll()).thenReturn(Arrays.asList(schoolClass));
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));

        // Save room, school class, and course
        roomRepository.save(room);
        schoolClassRepository.save(schoolClass);
        courseRepository.save(course);

        // Setup ScheduleRequestDTO
        ScheduleRequestDTO scheduleRequestDTO = new ScheduleRequestDTO();
        scheduleRequestDTO.setGranularity(180);
        scheduleRequestDTO.setCourses(Arrays.asList(new CourseDTO()));
        scheduleRequestDTO.setWeekdays(Arrays.asList(1, 2, 3, 4, 5));

        RoomsRequestDTO roomsRequestDTO = new RoomsRequestDTO();
        roomsRequestDTO.setShouldDeleteRooms(false);
        roomsRequestDTO.setData(Arrays.asList(new RoomDTO()));

        SchoolClassesRequestDTO schoolClassesRequestDTO = new SchoolClassesRequestDTO();
        schoolClassesRequestDTO.setShouldDeleteSchoolClass(false);
        schoolClassesRequestDTO.setData(Arrays.asList(new SchoolClassDTO()));

        scheduleRequestDTO.setRooms(roomsRequestDTO);
        scheduleRequestDTO.setSchoolClasses(schoolClassesRequestDTO);

        // Mock schedule repository save
        Schedule generatedSchedule = new Schedule();
        generatedSchedule.setRoom(room);
        generatedSchedule.setSchoolClass(schoolClass);
        generatedSchedule.setTimeSlot(new TimeSlot());

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(generatedSchedule);

        // When
        ScheduleResponseDTO response = scheduleService.generateSchedule(scheduleRequestDTO, true);

        // Ensure findAll returns at least one value
        List<Room> foundRooms = roomRepository.findAll();
        assertFalse(foundRooms.isEmpty(), "Room repository findAll should return at least one value");

        List<SchoolClass> foundSchoolClasses = schoolClassRepository.findAll();
        assertFalse(foundSchoolClasses.isEmpty(), "School class repository findAll should return at least one value");

        List<Course> foundCourses = courseRepository.findAll();
        assertFalse(foundCourses.isEmpty(), "Course repository findAll should return at least one value");

        // Then
        List<Schedule> generatedSchedules = response.getSchedule();
        assertEquals(1, generatedSchedules.size());

        // Verify the save method is called once
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    public void testAddSchedule() {
        Schedule schedule = new Schedule();
        schedule.setTitle("Test Schedule");

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule result = scheduleService.addSchedule(schedule);

        assertNotNull(result);
        assertEquals("Test Schedule", result.getTitle());
    }

    @Test
    public void testGetScheduleById() {
        Schedule schedule = new Schedule();
        schedule.setId(1L);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        Optional<Schedule> result = scheduleService.getScheduleById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    public void testGetAllSchedules() {
        Schedule schedule1 = new Schedule();
        Schedule schedule2 = new Schedule();

        when(scheduleRepository.findAll()).thenReturn(Arrays.asList(schedule1, schedule2));

        List<Schedule> result = scheduleService.getAllSchedules();

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteScheduleById() {
        doNothing().when(scheduleRepository).deleteById(1L);

        scheduleService.deleteScheduleById(1L);

        verify(scheduleRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateSchedule() {
        Schedule schedule = new Schedule();
        schedule.setTitle("Updated Schedule");

        when(scheduleRepository.existsById(1L)).thenReturn(true);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule result = scheduleService.updateSchedule(1L, schedule);

        assertNotNull(result);
        assertEquals("Updated Schedule", result.getTitle());
    }
}
